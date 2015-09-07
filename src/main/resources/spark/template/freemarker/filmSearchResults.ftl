<form action = "/film" method = "post">
    <input type="text" name="filmTitle"/> <input type="submit" name="add" value="Add a Film" />
    ${message}
</form>

<form action = "/filmSearch" method = "get">
    <input type="text" name="filmTitleSearch"/> <input type="submit" name="add" value="Search for a Film" />
</form>

<h2>Search Results</h2>
<table>
    <th>
        <td>IMDB ID</td> <td>Title</td> <td>Year</td> <td>Plot</td>
    </th>
    <tr>
        <#if idIMDB??><td>${idIMDB}</td></#if>
        <#if title??><td>${title}</td></#if>
        <#if year??><td>${year}</td></#if>
        <#if plot??><td>${plot}</td></#if>
    </tr>
</table>