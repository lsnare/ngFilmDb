<html>
    <body>
        <#include "index.ftl">
        <form action = "/search" method = "post">
            <input type="text" name="filmTitleSearch"/> <input type="submit" name="add" value="Search for a Film" />
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            <table border=1>
                <tr>
                    <th>IMDB ID</th>
                    <th>Title</th>
                    <th>Year</th>
                    <th>Plot</th>
                </tr>
                <tr>
                    <#if idIMDB??>${idIMDB}</#if>
                    <#if title??>${title}</#if>
                    <#if year??>${year}</#if>
                    <#if plot??>${plot}</#if>
                </tr>
            </table>
        </#if>
    </body>
</html>
