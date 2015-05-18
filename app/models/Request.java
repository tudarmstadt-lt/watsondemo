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
 *         - Uli Faher
 */

package models;

import play.i18n.Messages;
import util.ValidateUtil;

import java.util.ArrayList;
import java.util.List;

public class Request {

    public String question;

    public int numberOfAnswers;
    
    public Request() {}
    
    public  Request(String question, int numberOfAnswers) {
        this.question = question;
        this.numberOfAnswers = numberOfAnswers;
    }

    public static List<String> numbersOfAnswers() {
        List<String> choices = new ArrayList();
        for (int i = 5; i >= 1; i--) {
            choices.add("" + i);
        }
        return choices;
    }

    public String validate() {
        if (ValidateUtil.isBlank(question)) {
            return Messages.get("error.noquestion");
        }

        return null;
    }
}
