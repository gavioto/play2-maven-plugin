/*
 * Copyright 2012 Steve Chaloner
 *
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
 */
package controllers;

import actions.CustomRestrict;
import actions.RoleGroup;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.mvc.Controller;
import play.mvc.Result;
import security.MyRoles;
import views.html.accessOk;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Restrict({@Group("foo"),
           @Group("bar")})
public class RestrictController extends Controller
{
    public static Result index()
    {
        return ok(accessOk.render());
    }

    @Restrict({@Group({"foo", "bar"})})
    public static Result restrictOne()
    {
        return ok(accessOk.render());
    }

    @Restrict({@Group({"hurdy", "gurdy"}), @Group("foo")})
    public static Result restrictTwo()
    {
        return ok(accessOk.render());
    }

    @Restrict({@Group("foo"), @Group("!bar")})
    public static Result restrictThree()
    {
        return ok(accessOk.render());
    }

    @Restrict(@Group({"hurdy", "foo"}))
    public static Result restrictFour()
    {
        return ok(accessOk.render());
    }

    @Restrict(@Group({"foo", "!bar"}))
    public static Result restrictFive()
    {
        return ok(accessOk.render());
    }


    @CustomRestrict(value = { @RoleGroup({MyRoles.foo, MyRoles.bar}),
                                  @RoleGroup(MyRoles.hurdy)},
                        config = @Restrict({}))
    public static Result customRestrictOne()
    {
        return ok(accessOk.render());
    }

    @CustomRestrict(value = { @RoleGroup({MyRoles.hurdy, MyRoles.foo}),
                                  @RoleGroup({MyRoles.hurdy, MyRoles.bar})},
                        config = @Restrict({}))
    public static Result customRestrictTwo()
    {
        return ok(accessOk.render());
    }
}
