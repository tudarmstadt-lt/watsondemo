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

import play.i18n.Messages;
import util.AppException;

import static play.data.validation.Constraints.*;

/**
 * Login class used by Login Form.
 */
public class Login {
    
    @Required
    public String username;
    @Required
    public String password;

    /**
     * Validate the authentication. This method is called by the form framework.
     * See: https://www.playframework.com/documentation/2.0/JavaForms
     *
     * @return null if validation ok, string with details otherwise
     */
    @SuppressWarnings("unused")
    public String validate() {
        
        User user;
        try {
            user = User.authenticate(username, password);
        } catch (AppException e) {
            return Messages.get("error.technical");
        }
        if (user == null) {
            return Messages.get("invalid.user.or.password");
        }
        return null;
    }
}
