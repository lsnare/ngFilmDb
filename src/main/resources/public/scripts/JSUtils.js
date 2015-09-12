function showLongPlot(shortId, longId) {
    var shortPlot = document.getElementById(shortId);
    var longPlot = document.getElementById(longId);

    shortPlot.setAttribute("type", "hidden");
    longPlot.setAttribute("type", "none");
}