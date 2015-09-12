function showLongPlot(rowId) {
    shortPlot = document.getElementById(rowId).getElementsByTagName("td")[3].style.display="none"; ;
    longPlot = document.getElementById(rowId).getElementsByTagName("td")[4].style.display="block"; ;
}