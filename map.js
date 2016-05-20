var map;

function initMap() {
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        alert("Geolocation is not supported by this browser.");
        originalMap();
    }
}

function originalMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: -34.397, lng: 150.644},
		zoom: 9
	});
}

function showPosition(position) {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: position.coords.latitude, lng: position.coords.longitude},
		zoom: 9
	});
}

function changeLocation() {
	map.panTo(new google.maps.LatLng(-34.397, 150.644));
}