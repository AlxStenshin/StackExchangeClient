<!DOCTYPE html>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <head>
    <style>
        .label {
            color: white;
            border-radius: 4px;
            border: none;
            padding: 8px 16px;
            text-align: center;
            font-size: 16px;
            margin: 4px 2px;
        }
        .info {background-color: #2196F3;} /* Blue */
        .warning {background-color: #ff9800;} /* Orange */
        .danger {background-color: #f44336;} /* Red */
        .title {color: black;}

        input[type=text] {
            width: 60%;
            box-sizing: border-box;
            border: 2px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
            background-color: white;
            background-position: 10px 10px;
            background-repeat: no-repeat;
            padding: 12px 20px;
            margin: 8px 0;
        }

        .button {
            background-color: #2196F3;
            border: none;
            color: white;
            padding: 8px 16px;
            border-radius: 4px;
            text-align: center;
            font-size: 16px;
            margin: 4px 2px;
            opacity: 1;
            transition: 0.3s;
            display: inline-block;
            text-decoration: none;
            cursor: pointer;
        }

        .button:hover {opacity: 0.8}
        .button.disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }

        table {
            border-collapse: collapse;
            alignment: center;
            width: 75%;
        }

        th, td {
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even){background-color: #f2f2f2}
        th {
            background-color: #2196F3;
            color: white;
        }

    </style>
    <title>Stack Exchange API Client</title>
</head>

<body>

<script>
    function getAllUrlParams(url) {
        var queryString = url ? url.split('?')[1] : window.location.search.slice(1);
        var obj = {};
        if (queryString) {
            queryString = queryString.split('#')[0];
            var arr = queryString.split('&');
            for (var i = 0; i < arr.length; i++) {
                var a = arr[i].split('=');
                var paramName = a[0];
                var paramValue = typeof (a[1]) === 'undefined' ? true : a[1];
                paramName = paramName.toLowerCase();
                if (typeof paramValue === 'string') paramValue = paramValue.toLowerCase();
                if (paramName.match(/\[(\d+)?\]$/)) {
                    var key = paramName.replace(/\[(\d+)?\]/, '');
                    if (!obj[key]) obj[key] = [];
                    if (paramName.match(/\[\d+\]$/)) {
                        var index = /\[(\d+)\]/.exec(paramName)[1];
                        obj[key][index] = paramValue;
                    } else {
                        obj[key].push(paramValue);
                    }
                } else {
                    if (!obj[paramName]) {
                        obj[paramName] = paramValue;
                    } else if (obj[paramName] && typeof obj[paramName] === 'string'){
                        obj[paramName] = [obj[paramName]];
                        obj[paramName].push(paramValue);
                    } else {
                        obj[paramName].push(paramValue);
                    }
                }
            }
        }
        return obj;
    }
</script>
    <div>
        <label for="title" class="label title">Title input:</label><input type="text" id="title" value=""/>
        <button class="button" id="send_request" type="submit" onclick="search()">Search</button>
        <script>
            const input = document.getElementById("title");
            input.addEventListener("keypress", function(event) {
                if (event.key === "Enter") {
                    event.preventDefault();
                    document.getElementById("send_request").click();
                }
            });
        </script>
        <script>
            function search(){
                window.location='/questions?title=' + document.getElementById("title").value;
            }
        </script>
    </div>
    <div>
        <#if questions?has_content>
            <#if questions?size gt 0>
            <table>
                <tr>
                    <th>Date</th>
                    <th>Title</th>
                    <th>Owner</th>
                </tr>
                <#list questions as question>
                    <tr <#if question.answered>style="background-color:lightgreen"</#if>>
                        <td>${question.date?datetime!""}</td>
                        <td><a href="${question.link}" target="_blank"> ${question.title!""}</a></td>
                        <td>${question.ownerName!""}</td>
                    </tr>
                </#list>
            </table>

            <span>
                <button class="button" id="prev" type="submit" onclick="prev()">Prev</button>
                <script>
                    function prev(){
                        let pageId = getAllUrlParams().page;
                        if(isNaN(pageId))
                            window.location='/questions?title=' + getAllUrlParams().title + '&page=0';
                        else
                            window.location='/questions?title=' + getAllUrlParams().title + '&page=' + --pageId;
                    }
                </script>

                <button <#if !has_more!false> class="button disabled" <#else> class="button" </#if> id="next" type="submit" onclick="next()">Next</button>
                <script>
                    function next(){
                        let pageId = getAllUrlParams().page;
                        if(isNaN(pageId))
                            window.location='/questions?title=' + getAllUrlParams().title + '&page=2';
                        else
                            window.location='/questions?title=' + getAllUrlParams().title + '&page=' + ++pageId;
                    }
                </script>
            </span>
            </#if> <!-- questions size > 0-->
        </#if> <!-- questions has content -->
    </div>
    <p>
        <#if quota_remaining?has_content>
            <#if quota_remaining/quota_max < 0.25>
                    <span class="label warning">Quota: ${quota_remaining} of ${quota_max}<span>
                    <#else>
                    <span class="label info">Quota: ${quota_remaining} of ${quota_max}<span>
            </#if>
        </#if>
        <#if error?has_content><span class="label danger">Error: ${error}</span> </#if>
    </p>
</body>
