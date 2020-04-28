package com.freakycoder.casino.utils;

import com.freakycoder.casino.models.Queries;
import javaslang.Tuple;
import javaslang.collection.List;
import javaslang.collection.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;

@Component
public class QueryParser {

    private Map<String, Queries> queries;

    @PostConstruct
    public void init() throws Exception {
        queries = loadQueries(new ClassPathResource("queries.xml").getInputStream());
    }


    private Map<String, Queries> loadQueries(InputStream queriesXML) {
        try {
            SAXParserFactory parserFactor = SAXParserFactory.newInstance();
            parserFactor.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            SAXParser parser = parserFactor.newSAXParser();
            QueryXMLHandler handler = new QueryXMLHandler();
            parser.parse(queriesXML, handler);
            return List.ofAll(handler.queries).toMap(q -> Tuple.of(q.getId(), q));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public String forId(String id) {
        return queries.get(id).getOrElseThrow(
                () -> new RuntimeException(String.format("Query with id %s not found!", id)))
                .getSql();
    }

    static class QueryXMLHandler extends DefaultHandler {

        private java.util.List<Queries> queries = new ArrayList<>();
        private QueryBuilder tmpQuery;
        private StringBuilder tmpText = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("query".equals(qName)) {
                tmpQuery = new QueryBuilder();
                tmpText = new StringBuilder();
                tmpQuery.setId(attributes.getValue("id"));
                tmpQuery.setComment(attributes.getValue("comment"));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            tmpText.append(String.copyValueOf(ch, start, length));
        }


        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("query".equals(qName)) {
                tmpQuery.setSql(tmpText.toString());
                tmpText = new StringBuilder();
                queries.add(tmpQuery.create());
            }
        }
    }
}
