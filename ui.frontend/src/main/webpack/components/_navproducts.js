// Example of how a component should be initialized via JavaScript
// This script logs the value of the component's text property model message to the console

(function() {
    "use strict";

    console.log("NAV PRODUCTS WORKED!!!")
    fetch('http://localhost:4502/bin/myCustData')
    .then(function(response){
        let data = response.json();
        console.log(data);
    })
    

}());
