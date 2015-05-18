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

package models;


import jwatson.answer.Evidencelist;
import jwatson.answer.WatsonAnswer;
import jwatson.question.WatsonQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Response {

    private final static double confidenceThreshold = 10E-4;

    protected Optional<WatsonAnswer> answer;
    protected Optional<WatsonQuestion> question;
    protected List<WatsonQuestion> similarQuestions;

    public Response(){
        //Default
    }

    public Response(WatsonAnswer answer, WatsonQuestion question) {
        this.answer = Optional.of(answer);
        this.question = Optional.of(question);
        this.similarQuestions = new ArrayList<>();
    }

    public List<Evidencelist> getFilteredEvidences() {
        int numberOfAnswers = question.get().getQuestion().getNumberOfAnswers();

        return answer.get().getAnswerInformation().getEvidencelist().stream()
                .filter(e -> e.getValue() >= confidenceThreshold)
                .limit(numberOfAnswers)
                .collect(Collectors.toList());
    }

    public List<WatsonQuestion> getSimilarQuestions() {
        return similarQuestions;
    }

    public void setSimilarQuestions(List<WatsonQuestion> similarQuestions) {
        this.similarQuestions = similarQuestions;
    }

    public static class EmptyResponse extends Response {

        public EmptyResponse(WatsonQuestion question) {
            this.answer = Optional.empty();
            this.question = Optional.of(question);
        }

        public EmptyResponse() {
            this.answer = Optional.empty();
            this.question = Optional.empty();
        }

        @Override
        public List<Evidencelist> getFilteredEvidences() {
            return new ArrayList<>();
        }

        @Override
        public List<WatsonQuestion> getSimilarQuestions() {
            return new ArrayList<>();
        }
    }
}