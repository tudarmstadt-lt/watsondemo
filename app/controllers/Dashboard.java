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

package controllers;

import com.google.inject.Inject;
import jwatson.answer.WatsonAnswer;
import jwatson.question.WatsonQuestion;
import models.Question;
import models.Request;
import models.Response;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dashboard;

import java.util.List;

import static play.data.Form.form;

@Security.Authenticated(ActionAuthenticator.class)
public class Dashboard extends Controller {

    @Inject
    private IWatsonService watsonService;

    public Result index() {
        return ok(dashboard.render(form(Request.class), User.findByUsername(request().username()), new Response.EmptyResponse()));
    }

    public Result askByForm() {
        Form<Request> requestForm = form(Request.class).bindFromRequest();

        return ask(requestForm);
    }

    public Result askByString(String questionText) {
        Form<Request> requestForm = form(Request.class);
        requestForm = requestForm.fill(new Request(questionText, 5));

        return ask(requestForm);
    }

    private Result ask(Form<Request> request) {
        User user = User.findByUsername(ctx().session().get("username"));

        if (request.hasErrors()) {
            return badRequest(dashboard.render(request, user, new Response.EmptyResponse()));
        }

        WatsonQuestion question = new WatsonQuestion.QuestionBuilder(request.get().question)
                .setNumberOfAnswers(request.get().numberOfAnswers)
                .create();


        WatsonAnswer answer = watsonService.getInstance().askQuestion(question);
        Response response = new Response(answer, question);

        if (hasAnswer(response)) {
            String questionText = question.getQuestion().getQuestionText();
            Question questionEntry = user.addQuestion(questionText);

            List<WatsonQuestion> similar = Question.findSimilar(questionEntry);
            response.setSimilarQuestions(similar);

            return ok(dashboard.render(request, user, response));
        } else {
            request.reject("No answers found");
            return ok(dashboard.render(request, user, new Response.EmptyResponse(question)));
        }
    }


    public boolean hasAnswer(Response response) {
        final String banned = "redp4955"; // IBM document
        response.getFilteredEvidences().removeIf(e -> e.getTitle().startsWith(banned));

        return !response.getFilteredEvidences().isEmpty();
    }
}
