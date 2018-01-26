/*
 * The contents of this file are subject to the terms of the Common Development and
 *  Distribution License (the License). You may not use this file except in compliance with the
 *  License.
 *
 *  You can obtain a copy of the License at https://forgerock.org/license/CDDLv1.0.html. See the License for the
 *  specific language governing permission and limitations under the License.
 *
 *  When distributing Covered Software, include this CDDL Header Notice in each file and include
 *  the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 *  Header, with the fields enclosed by brackets [] replaced by your own identifying
 *  information: "Portions copyright [year] [name of copyright owner]".
 *
 *  Copyright 2018 ForgeRock AS.
 *
 */
package com.forgerock.openbanking.sample.tpp.api.hello;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "hello world", description = "test the installation of the app by doing an hello world!")
@RequestMapping("/helloworld/")
public interface HelloWorld {

    @ApiOperation(value = "Say hello to the application",
            notes = "test the installation of the app by doing an hello world!", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return 'Hello World!'", response = String.class)
        })
    @RequestMapping(value = "/", method = RequestMethod.GET)
    ResponseEntity<String> helloWorld();

}
