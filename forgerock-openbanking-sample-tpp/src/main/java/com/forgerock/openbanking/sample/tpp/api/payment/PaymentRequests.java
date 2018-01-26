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
package com.forgerock.openbanking.sample.tpp.api.payment;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Api(value = "payment requests", description = "Payment requests API")
@RequestMapping("/payments/")
public interface PaymentRequests {

    @ApiOperation(value = "Initiate a payment request",
            notes = "In order to use the payment APIs, you will need to initiate an payment request. This endpoint " +
                    "will allow you to trigger this flow.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The payment request was successfully created. As a result, you will " +
                    "receive a URI where you will need to redirect the user to. (POST can't trigger a 302 :( )",
                    response = String.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
            @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })
    @RequestMapping(value = "initiatePayment", method = RequestMethod.POST)
    ResponseEntity<String> initiatePayment(
            @ApiParam(value = "The ASPSP ID", required = true)
            @RequestParam(value = "aspspId") String aspspId
    );

    @ApiOperation(value = "Exchange the OIDC authorization code",
            notes = "This endpoint is called by the ASPSP-AS, when redirecting the authorization code responses " +
                    "to the OIDC client. UIs should not try to consumme this endpoint.",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 302, message = "The user is redirected to the successful uri or the failure one, " +
                    "depending of the situation that happened."),
    })
    @RequestMapping(value = "exchange_code", method = RequestMethod.GET)
    ResponseEntity<String> exchangeCode(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "id_token") String idToken,
            @RequestParam(value = "state") String state,
            HttpServletResponse response);

}
