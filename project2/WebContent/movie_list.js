/**
 * 
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */


function handleResult(resultData) {
	/*
    console.log("handleResult: populating star info from resultData");
    // populate the star info h3
    //find the empty h3 body by id "star_info"
    let starInfoElement = jQuery("#star_info");

    // append two html <p> created to the h3 body, which will refresh the page
    starInfoElement.append("<p>Star Name: " + resultData[0]["star_name"] + "</p>" +
        "<p>Date Of Birth: " + resultData[0]["star_dob"] + "</p>");
	
    console.log("handleResult: populating movie table from resultData");
    */

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    
    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(resultData[i]["limit"], resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movieId"] + "</th>";
        rowHTML += "<th><a href='single-movie.html?id=" + resultData[i]["movieId"]; 
        rowHTML += "'>" + resultData[i]["movieTitle"] + "</a></th>";
        rowHTML += "<th>" + resultData[i]["movieYear"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movieDirector"] + "</th>";
        rowHTML += "<th>";
        var genre = resultData[i]["genreNames"];
        var genreArray = genre.split(',');
        for(var z = 0; z < genreArray.length; z++)
        	{
        	if(z == (genreArray.length-1))
        		{
        		rowHTML += genreArray[z];
        		break;
        		}	
        	rowHTML += genreArray[z] + ", ";
        	}
        rowHTML += "</th>";
        rowHTML += "<th>";
        var star = resultData[i]["starNames"];
        var starArray = star.split(',');
        for(var x = 0; x < starArray.length; x++)
        	{
        	if(x == (starArray.length-1))
        		{
        		rowHTML += "<a href='single-star.html?starName=" + starArray[x] + "'>" + starArray[x] + "</a>";
        		break;
        		}	
        	rowHTML += "<a href='single-star.html?starName=" + starArray[x] + "'>" + starArray[x] + "</a>, ";
        	}
        rowHTML += "</th>";
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
        let sortByTitle = jQuery("#sorting");
        
        var sortByTitleLink = "Sort by: <a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=asc_title'>Ascending Title</a>, ";
        sortByTitleLink += "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=desc_title'>Descending Title</a>, ";
        sortByTitleLink += "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=asc_rating'>Ascending Rating</a>, ";
        sortByTitleLink += "<a href='movie_list.html?title=" + title + "&year=" + year + "&limit=" + limit + "&offset=" + offset;
        sortByTitleLink += "&director=" + director + "&starName=" +  starName + "&genre=" + genre + "&genreId" + genreId;
        sortByTitleLink += "&letter=" + letter + "&order=desc_rating'>Descending Rating</a>";
        
        sortByTitle.append(sortByTitleLink);
        
        /*
        function myFunction() {
            document.getElementById("sortByTitle").classList.toggle("show");
        }

        window.onclick = function(event) {
        	  if (!event.target.matches('.dropbtn')) {

        	    var dropdowns = document.getElementsById("sortByTitle");
        	    var i;
        	    for (i = 0; i < dropdowns.length; i++) {
        	      var openDropdown = dropdowns[i];
        	      if (openDropdown.classList.contains('show')) {
        	        openDropdown.classList.remove('show');
        	      }
        	    }
        	  }
        }
        
}
*/
}
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let starName = getParameterByName('starName');
let genre = getParameterByName('genre');
let genreId = getParameterByName('genreId');
let letter = getParameterByName('letter'); 
let order = getParameterByName('order');
let limit = getParameterByName('limit');
let offset = getParameterByName('offset');

/*
var win = window.open();
win.document.write(title);
win.document.write(year);
win.document.write(director);
win.document.write(starName);
win.print();
win.close();
*/

//Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "MovieListServlet?title=" +title +"&year=" +year +"&director=" +director +"&starName=" +starName +"&genre=" +genre + "&genreId=" +genreId + "&letter=" +letter + "&order=" +order + "&limit=" +limit + "&offset=" +offset, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});