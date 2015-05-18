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

import jwatson.question.WatsonQuestion;
import play.db.ebean.Model;
import util.CollectionUtil;
import util.SimilarityScorer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Question extends Model {

    @Id
    public Integer id;
    public String text;

    @ManyToMany(mappedBy = "questionList")
    public List<User> owner = new ArrayList<>();

    public static Model.Finder<Integer, Question> find = new Model.Finder(Integer.class, Question.class);

    private Question(String text) {
        this.text = text;
    }

    public static Question create(String questionText) {
        Question question = find.select("id, text").where().eq("text", questionText).findUnique();

        if(question != null) {
            return question;
       }

        question = new Question(questionText);
        question.save();
        
        return question;
    }

    public static List<WatsonQuestion> findSimilar(Question question) {
        List<Question> questions = find.select("id, text").findList();
        
        SimilarityScorer sim = new SimilarityScorer(questions);
        Map<Integer, Double> scores = sim.scoreQuestions(question);

        //Oh my, current ebean version don't support lambda's in model classes.
        //This is fixed in ebean 4.x. Wait for new Play Framework version.
        /*List<WatsonQuestion> selection = scores.entrySet().stream()
                .filter(p -> p.getValue() >= 0.5 && p.getKey().intValue() != question.id)
                .sorted((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                .limit(4)
                .map(p -> {
                    Question model = find.select("id, text").where().eq("id", p.getKey()).findUnique();
                    return new WatsonQuestion.QuestionBuilder(model.text).create();
                })
                .collect(Collectors.toList());
        
        return selection;*/

        List<Map.Entry<Integer, Double>> selection = CollectionUtil.findGreatest(scores, 4);
        
        List<WatsonQuestion> result = new ArrayList<>();
        for(Map.Entry<Integer, Double> item: selection) {

            if(item.getValue() < 0.5 || item.getKey().intValue() == question.id) {
                continue;
            }
            
            Question model = find.select("id, text").where().eq("id", item.getKey()).findUnique();
            result.add(new WatsonQuestion.QuestionBuilder(model.text).create());
        }
        
        return result;
    }
}
