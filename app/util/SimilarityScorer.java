/*
 * Copyright 2015 Technische Universitaet Darmstadt
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *         - Uli Fahrer
 */

package util;


import models.Question;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.CosineSimilarity;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SimilarityScorer {

    private CosineSimilarity similarity = new CosineSimilarity();

    private List<Question> questions;
    private List<List<String>> documents;
    private List<String> allWords;

    public SimilarityScorer(List<Question> questions) {
        this.questions = questions;
        //TODO: without stopwords, but preverse is, of, ...
        documents = getDocuments();
        allWords = getAllWords(documents);
    }

    public Map<Integer, Double> scoreQuestions(Question question) {
        
        if(questions == null || questions.isEmpty()) {
            return new HashMap<>();
        }
        
        Instance compareVector = questionToVector(question);

        Map<Integer, Double> result = new HashMap<>();
        for (Question q : questions) {
            Instance documentVector = questionToVector(q);

            double score = similarity.measure(compareVector, documentVector);
            result.put(q.id, score);
        }
        
        return result;
    }

    private List<List<String>> getDocuments() {
        List<List<String>> documents = new ArrayList<>();

        for (Question question : questions) {
            String lower = StringUtils.lowerCase(question.text);
            List<String> token = NLPUtil.stems(lower);
            documents.add(token);
        }
        return documents;
    }

    private List<String> getAllWords(List<List<String>> documents) {
        return documents.stream().flatMap(doc -> doc.stream()).collect(Collectors.toList());
    }

    private Instance questionToVector(Question question) {
        Map<String, Double> tf = getTermFrequencies(question.text);
        double[] vector = new double[allWords.size()];

        for (int i = 0; i < vector.length; i++) {
            String word = allWords.get(i);
            vector[i] = tf.containsKey(word) ? tf.get(word) : 0.0;
        }
        
        return new SparseInstance(vector, question);
    }

    private Map<String, Double> getTermFrequencies(String document) {
        String lower = document.toLowerCase();
        List<String> token = NLPUtil.stems(lower);

        Map<String, Double> termFrequency = new HashMap<>();
        for (String word : token) {
            Double freq = termFrequency.containsKey(word) ? termFrequency.get(word) : 0.0;
            termFrequency.put(word, freq + 1);
        }

        return termFrequency;
    }
}
