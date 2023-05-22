/*
 * Web engineering course - University of L'Aquila
 * RESTful resources testing code
 *  
 */


"use strict";

function Restest(testall = true) {
    let bearer_token = null;
    let tests_ok_count = 0;
    let tests_error_count = 0;
    let token_waiting_list = [];
    let THIS = this;

    this.getErrors = function () {
        return tests_error_count;
    };

    this.getToken = function () {
        return bearer_token;
    };

    let setToken = function (token) {
        bearer_token = token;
        let tokenf = document.getElementById("token-field");
        if (tokenf) {
            tokenf.value = token;
        }
    };

    let extractTokenFromHeader = function (header) {
        return header.substring("Bearer".length).trim();
    };

    let sendRestRequest = function (method, url, callback, acceptType = null, payload = null, payloadType = null, token = null, async = true) {
        let xhr = new XMLHttpRequest();
        xhr.open(method, url, async);
        if (token !== null)
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        if (payloadType !== null)
            xhr.setRequestHeader("Content-Type", payloadType);
        if (acceptType !== null)
            xhr.setRequestHeader("Accept", acceptType);
        if (async) {
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    callback(xhr.responseText, xhr.status, xhr.getResponseHeader("Authorization"));
                }
            };
        }
        xhr.send(payload);
        if (!async) {
            callback(xhr.responseText, xhr.status, xhr.getResponseHeader("Authorization"));
    }
    };


    let makeDefaultResponseCallback = function (params) {
        return function (callResponse, callStatus, callAuthHeader) {
            console.log(params.method + " " + params.url + " response: " + callStatus + ": " + callResponse.substring(0, 50));
            if (params.responseTarget) {
                params.responseTarget.textContent = callStatus + ": " + callResponse;
            }
            if (params.source) {
                params.source.classList.remove("rest-test-wait");
            }
            if ((params.responseStatus === null || (callStatus == params.responseStatus))
                    && (params.responseText === null || (callResponse === params.responseText))) {
                if (params.source) {
                    params.source.classList.add("rest-test-ok");
                    tests_ok_count++;
                }
                if (params.responseHasToken && callAuthHeader !== null) {
                    let token = extractTokenFromHeader(callAuthHeader);
                    console.log("got bearer token: " + token);
                    setToken(token);
                    testAuthItems();
                }
            } else {
                if (params.source) {
                    tests_error_count++;
                    params.source.classList.add("rest-test-error");
                    params.source.setAttribute("data-test-out-response", callResponse);
                    params.source.setAttribute("data-test-out-status", callStatus);
                    params.source.setAttribute("title", callStatus + " " + callResponse);
                }
            }
            if (params.source) {
                if (tests_error_count > 0) {
                    document.body.classList.add("rest-test-error-all");
                    document.body.classList.remove("rest-test-ok-all");
                } else {
                    document.body.classList.remove("rest-test-error-all");
                    document.body.classList.add("rest-test-ok-all");
                }
            }
        };
    };

    let testAuthItems = function () {
        for (let i = 0; i < token_waiting_list.length; ++i) {
            THIS.makeRESTcall(token_waiting_list[i]);
        }
    };

    ///////////////////// public object methods

    this.makeRESTcall = function (params, responseCallback = null, async = true) {
        if (responseCallback === null) {
            //default response callback
            responseCallback = makeDefaultResponseCallback(params);
        }
        if (params.source) {
            params.source.classList.add("rest-test-wait");
        }
        console.log("calling " + params.method + " " + params.url + (params.needsAuthorization ? " with token " + bearer_token : "") + (params.responseHasToken ? " (getting bearer token)" : ""));
        sendRestRequest(params.method, params.url, responseCallback, params.acceptType, params.payload, params.payloadType, bearer_token, async);
    };

    this.testAllItems = function () {
        token_waiting_list = [];
        let tl = document.querySelectorAll("[data-rest-test-url], [href][data-rest-test]");
        for (let i = 0; i < tl.length; ++i) {
            let element = tl.item(i);

            let target = null;
            if (element.hasAttribute("data-rest-test-target")) {
                target = element.getAttribute("data-rest-test-target");
                if (target !== "") {
                    target = document.querySelector(target);
                } else {
                    let enclosing_tr = element.closest("tr");
                    if (enclosing_tr !== null && enclosing_tr.querySelector(".output") !== null) {
                        target = enclosing_tr.querySelector(".output");
                    } else {
                        target = null;
                    }
                }
            }

            let params = {
                source: tl.item(i),
                url: element.hasAttribute("href") ? element.getAttribute("href") : element.getAttribute("data-rest-test-url"),
                method: element.hasAttribute("data-rest-test-method") ? element.getAttribute("data-rest-test-method") : "GET",
                payload: element.hasAttribute("data-rest-test-payload") ? element.getAttribute("data-rest-test-payload") : null,
                payloadType: element.hasAttribute("data-rest-test-content-type") ? element.getAttribute("data-rest-test-content-type") : null,
                acceptType: element.hasAttribute("data-rest-test-accept") ? element.getAttribute("data-rest-test-accept") : null,
                needsAuthorization: element.hasAttribute("data-rest-test-auth"),
                responseText: element.hasAttribute("data-rest-test-response") ? element.getAttribute("data-rest-test-response") : null,
                responseStatus: element.hasAttribute("data-rest-test-status") ? element.getAttribute("data-rest-test-status") : 200,
                responseTarget: target,
                responseHasToken: element.hasAttribute("data-rest-test-token")
            };
            if (!params.needsAuthorization || bearer_token !== null) {
                THIS.makeRESTcall(params);
            } else {
                token_waiting_list.push(params);
            }
        }
    };

    /////////////////////

    let handleLoginButton = function () {
        let u = document.getElementById("username-field").value;
        let p = document.getElementById("password-field").value;
        sendRestRequest(
                "post", "rest/auth/login",
                function (callResponse, callStatus, callAuthHeader) {
                    if (callStatus === 200) {
                        setToken(extractTokenFromHeader(callAuthHeader));
                    } else {
                        setToken(null);
                    }
                },
                null,
                "username=" + u + "&password=" + p, "application/x-www-form-urlencoded",
                null);

    };

    let handleLogoutButton = function () {
        sendRestRequest(
                "delete", "rest/auth/logout",
                function (callResponse, callStatus) {
                    if (callStatus === 204) {
                        setToken(null);
                    }
                },
                null, null, null, bearer_token);
    };

    let handleRefreshButton = function () {
        sendRestRequest(
                "get", "rest/auth/refresh",
                function (callResponse, callStatus, callAuthHeader) {
                    if (callStatus === 200) {
                        setToken(extractTokenFromHeader(callAuthHeader));
                    } else {
                        setToken(null);
                    }
                },
                null, null, null, bearer_token);

    };

    /////////////////////

    let init = function () {
        //bind login/logout/refresh buttons, if present
        let loginb = document.getElementById("login-button");
        if (loginb)
            loginb.addEventListener("click", function (e) {
                handleLoginButton();
                e.preventDefault();
            });
        let logoutb = document.getElementById("logout-button");
        if (logoutb)
            logoutb.addEventListener("click", function (e) {
                handleLogoutButton();
                e.preventDefault();
            });
        let refreshb = document.getElementById("refresh-button");
        if (refreshb)
            refreshb.addEventListener("click", function (e) {
                handleRefreshButton();
                e.preventDefault();
            });


        //modify the <a> links that need an authorization header
        let nl = document.querySelectorAll("a[href][data-rest-test-auth]");
        for (let i = 0; i < nl.length; ++i) {
            let anchor = nl.item(i);
            anchor.addEventListener("click", function (e) {
                sendRestRequest(
                        "get", anchor.href,
                        function (callResponse, callStatus) {
                            if (callStatus === 200) {
                                document.body.innerText = callResponse;
                            } else {
                                document.body.innerText = "Status code: " + callStatus;
                            }
                        },
                        null, null, null, bearer_token);
                e.preventDefault();
            });
        }

        //
        if (testall) {
            THIS.testAllItems();
        }
    };

    init();
}
