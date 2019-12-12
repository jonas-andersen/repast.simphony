
import { empty } from '/js/utils.js';
import { VRButton } from '/vendor/VRButton.js';

import { FirstPersonVRControls } from '/vendor/FirstPersonVRControls.js';

//we use to set a valid width / height for a display
//the display's tab may not be visible yet if its not the
//first tab and so its clientWidth / clientHeight will be 0
//When that's the case, we can use these.
let uwidth = -1;
let uheight = -1;

let world;
let fpVrControls;
let buildingTileLayer;

var controller1, controller2;
var raycaster;
var intersected = [];
var tempMatrix = new THREE.Matrix4();

let agent_layer;
var shape = new THREE.SphereGeometry(100, 16, 16);
var material = new THREE.MeshStandardMaterial( { color: 0x1f78b4 } );

let mesh = new THREE.Mesh(shape, material);

function updateVRControls() {
	fpVrControls.update(world._engine.clock.getDelta());
	
	intersectObjects( controller1 );
	intersectObjects( controller2 );
	
    requestAnimationFrame(updateVRControls);
}

function updateLOD(){
	buildingTileLayer._calculateLOD();
}

async function fetchJSON(url = '', data = {}) {
	const response = await fetch(url);
	return await response.json(); // parses JSON response into native JavaScript objects
}

function randomGreyHex() {
	var v = (Math.random()*(256)|0).toString(16);//bitwise OR. Gives value in the range 0-255 which is then converted to base 16 (hex).
	return "#" + v + v + v;
}


class ShapeCreator {

    constructor(shape_name){
        this.shape_name = shape_name;
    }
    
    create(data) {
        
        var latlons = data.latlons;  // The latlon point array
        var item = null;
        
        // Line geometry
        if (this.shape_name === "line"){
//            item = L.polyline(latlons, {});
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
//            item = L.polygon(latlons, {}); 
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
//            item = L.shapeMarker(latlons[0], {
//					shape: this.shape_name,
//                    radius: 10
//			});
        }
     
        // Apply styling     
//        this.updateStyle(item,data);
     
        return item;
    }
    
    updateStyle(item, data) {
           
        // Line geometry
        if (this.shape_name === "line"){
           // TODO update styles
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
             // TODO update styles
           
//            item.setStyle({
//                fillColor: data.color,
//                weight: 1,
//                fillOpacity: 0.5,
//                color: '#000000'
//            });
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
            // TODO How to check for what style data is passed in ??
            
//            item.setStyle({
//                fillColor: data.color,
//                weight: 1,
//                fillOpacity: 0.5,
//                color: '#000000'
//            });
             
        }
    }
    
    updateLocation(item, data) { 
        var latlons = data.latlons;  // The latlon point array
        
        // Line geometry
        if (this.shape_name === "line"){
//           item.setLatLngs(latlons);
        }
        
        // Polygon geometry
        else if (this.shape_name === "polygon"){
//           item.setLatLngs(latlons);
        }
        
        // Point geometry
        // single coord pair [x,y] for point symbols
        else {
//            item.setLatLng(latlons[0]);
        }
    }

}

export class ViziCitiesDisplay {
    constructor(name, tab_content, display_id) {
        this.name = name;
        this.objectMap = new Map();

        this.tab_content = tab_content;
        
       let enable_vr = true;
        
        // TODO perhaps refactor the container setup to an abstract parent class
        // Get a reference to the container element that will hold our scene
      
        // Create the container that will hold this display
        this.container = document.createElement("div");
        this.container.className = "card-display-container";
        this.container.setAttribute("display_id", display_id);
        
        let header = document.createElement("h5");
        header.className = "card-header";
        header.textContent = name;
        this.container.appendChild(header);
        this.card_body = document.createElement("div");
        this.card_body.className = "card-block-display w-100 p-0";
        this.container.appendChild(this.card_body);
        
        tab_content.appendChild(this.container);
        
        // Use the display id which is unique, since the display name can be duplicate
        this.card_body.id = display_id;
        
        let width = this.card_body.clientWidth == 0 ? uwidth : this.card_body.clientWidth;
        let height = this.card_body.clientHeight == 0 ? uheight : this.card_body.clientHeight;
 
        // should be > 0 for first tab 
        uwidth = width;
        uheight = height;
                        
//        var coords = [40.739940, -73.988801];  // NYC
        var coords = [41.8758, -87.6189, 1000]; // Chicago
        
        world = VIZI.world(this.card_body.id, {
        	 skybox: true
        	 //postProcessing: true
        }).setView(coords);
        
        // Set position of sun in sky
        world._environment._skybox.setInclination(0.15);
        
        // Enable for VR
        if (enable_vr){
        	world._engine._renderer.vr.enabled = true; 

        	// Increase the camera FOV to minimize flickering in tile layers
        	world.getCamera().fov = 100;
        	world.getCamera().updateProjectionMatrix();
        	
        	// Provide a moveable rig that is the camera's parent.
        	var rig = new THREE.Object3D();
        	rig.add(world.getCamera());
        	world._engine._scene.add(rig);
        	
        	// Add controls to the rig
        	fpVrControls = new FirstPersonVRControls(world.getCamera(), world._engine._scene, rig);
        	// Optionally enable vertical movement.
        	fpVrControls.verticalMovement = true;
        	// Optionally enable strafing.
        	fpVrControls.strafing = true;

        	// Add VR hand controllers and attach to the rig 
			controller1 = world._engine._renderer.vr.getController( 0 );
			controller1.addEventListener( 'selectstart', onSelectStart );
			controller1.addEventListener( 'selectend', onSelectEnd );
			rig.add( controller1 );
			
			controller2 = world._engine._renderer.vr.getController( 1 );
			controller2.addEventListener( 'selectstart', onSelectStart );
			controller2.addEventListener( 'selectend', onSelectEnd );
			rig.add( controller2 );

			// Add a simple line pointer to the controls
			var geometry = new THREE.BufferGeometry().setFromPoints( [ new THREE.Vector3( 0, 0, 0 ), new THREE.Vector3( 0, 0, - 1 ) ] );
			var line = new THREE.Line( geometry );
			line.name = 'line';
			line.scale.z = 5;
			controller1.add( line.clone() );
			controller2.add( line.clone() );
			raycaster = new THREE.Raycaster();
        	
        	updateVRControls();
        }
        else {  // normal mouse camera control
        	VIZI.Controls.orbit().addTo(world);
        }
                
        this.init_static_layers();
        
        var features = [];
        
        var feature = {}
        feature ["type"] = "Feature";
        feature ["geometry"] = {"type" : "Point", "coordinates" : [-87.6189, 41.8758]};
        feature ["properties"] = {"name" : "MyAgent"};
        
        features.push(feature);
        
        var feature_collection = {"type" : "FeatureCollection",
        		"features" : features};
        
//        console.log(JSON.stringify(feature_collection));
        
//        var foo = world.project(coords);
//        
//        console.log(foo);
        
        agent_layer = VIZI.geoJSONLayer(feature_collection, {
    		output: true,
    		style: {
    			color: '#ff0000',
    			outline: true,
    			outlineColor: '#580000',
    			lineColor: '#0000ff',
    			lineRenderOrder: 1,
    			pointColor: '#00cc00'
    		},
    		pointGeometry: function(feature) {
    			var geometry = new THREE.SphereGeometry(100, 16, 16);
    			return geometry;
    		}
//    		,
//    		filter: function(feature) {
//    			// Don't show null
//    			return feature.geometry !== null;
//
//    		}
    	}).addTo(world);
        
//        agent_layer.add(mesh);
        
        window.addEventListener('resize', this.windowResize.bind(this));

        if (enable_vr){
        	this.container.appendChild( VRButton.createButton(world._engine._renderer ) );   
        	window.addEventListener('keydown', this._onKeyDown, false);
        	
        	setInterval(updateLOD, 100);
        }
    }
    
    async init_static_layers(){
    	// OSM Tile layer
//        VIZI.imageTileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(world);
      
    	
    	
    	let backgroundLayer = VIZI.imageTileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
    		distance: 8 * 300000 * VIZI.Geo.multiplier
    	});
    	
    	backgroundLayer._updateFrustum = function() {
    		    var camera = this._world.getCamera();
    		    var projScreenMatrix = new THREE.Matrix4();
    		    projScreenMatrix.multiplyMatrices(camera.projectionMatrix, camera.matrixWorldInverse);

    		    this._frustum.setFromMatrix(camera.projectionMatrix);
    		    this._frustum.setFromMatrix(new THREE.Matrix4().multiplyMatrices(camera.projectionMatrix, camera.matrixWorldInverse));
    		  }
    	
    	backgroundLayer.addTo(world);
    	
//    	VIZI.imageTileLayer('http://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}.png', {
//    		distance: 8 * 300000 * VIZI.Geo.multiplier
//    	}).addTo(world);
    	
    	// ESRI Gray Tile layer
//    	VIZI.imageTileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}', {
////    		maxLOD:25,
////    		minLOD:1,
////    		 maxCache: 10000
////    		distance: 8 * 300000 * VIZI.Geo.multiplier
//    	}).addTo(world);
      
    	// Buildings from Tilezen
    	buildingTileLayer = VIZI.geoJSONTileLayer('https://tile.nextzen.org/tilezen/vector/v1/all/{z}/{x}/{y}.json?api_key=-P8vfoBlQHWiTrDduihXhA', {
    		interactive: true,
    		maxCache: 10000,
    		maxLOD: 15,
//    	    distance: 1 * 100000 * VIZI.Geo.multiplier,
    		
    	    style: function(feature) {
    			var height;

    			if (feature.properties.height) {
    				height = feature.properties.height;
    			} else {
    				height = 10 + Math.random() * 10;
    			}

    			return {
    				height: height,
    				color: randomGreyHex()
    			};
    		},
    		layers: ['buildings'],
    		filter: function(feature) {
    			// Don't show points
    			return feature.geometry.type !== 'Point';
    		}
    	});
    	buildingTileLayer.addTo(world);

    	
     
//    	// NG Pipelines
//    	var pipe_data = await fetchJSON('testdata/Natural_Gas_Pipelines.geojson');
//        
//    	// Load via URL
////    	VIZI.geoJSONLayer('https://opendata.arcgis.com/datasets/f44e00fce8b943f69a40a2324cf49dfd_0.geojson', {
//    	VIZI.geoJSONLayer(pipe_data, {
//    		output: true,
//    		style: {
//    			color: '#ff0000',
//    			outline: true,
//    			outlineColor: '#580000',
//    			lineColor: '#0000ff',
//    			lineRenderOrder: 1,
//    			pointColor: '#00cc00'
//    		},
//    		pointGeometry: function(feature) {
//    			var geometry = new THREE.SphereGeometry(200, 16, 16);
//    			return geometry;
//    		},
//    		filter: function(feature) {
//    			// Don't show null
//    			return feature.geometry !== null;
//    		}
//    	}).addTo(world);
//
//    	// NG Compressor stations
//    	// Load via URL
////    	 VIZI.geoJSONLayer('https://opendata.arcgis.com/datasets/cb4ea4a90a5e4849860d0d56058c2f75_0.geojson', {
//    	// Need to await data since layer needs the actual geojson
//    	var data = await fetchJSON('testdata/Natural_Gas_Compressor_Stations.geojson');
//    	VIZI.geoJSONLayer(data, {
//    		output: true,
//    		style: {
//    			color: '#ff0000',
//    			outline: true,
//    			outlineColor: '#580000',
//    			lineColor: '#00ff00',
//    			lineRenderOrder: 1,
//    			pointColor: '#00cc00'
//    		},
//    		pointGeometry: function(feature) {
//    			var geometry = new THREE.SphereGeometry(200, 16, 16);
//    			return geometry;
//    		},
//    		filter: function(feature) {
//    			// Don't show null
//    			return feature.geometry !== null;
//    		}
//    	}).addTo(world);    	
// 
//    	
//    	// EP Transmission Lines
//    	var bes_data = await fetchJSON('testdata/Electric_Power_Transmission_Lines.geojson');
//        
//    	// Load via URL
////    	 VIZI.geoJSONLayer('https://opendata.arcgis.com/datasets/70512b03fe994c6393107cc9946e5c22_0.geojson', {
//    	VIZI.geoJSONLayer(bes_data, {
//    		output: true,
//    		style: {
//    			color: '#ff0000',
//    			outline: true,
//    			outlineColor: '#580000',
//    			lineColor: '#00ff00',
//    			lineRenderOrder: 1,
//    			pointColor: '#00cc00'
//    		},
//    		pointGeometry: function(feature) {
//    			var geometry = new THREE.SphereGeometry(200, 16, 16);
//    			return geometry;
//    		},
//    		filter: function(feature) {
//    			// Don't show null
//    			return feature.geometry !== null;
//    		}
//    	}).addTo(world);
    	
    	
    }  // end init_static_layers
    
 
    // Initialize the display
    init(msg) {
        
        console.log(msg);
        
        this.mapLayers = new Map();     // Holds a L.layerGroup for each layer
        this.itemCreators = new Map();  // Holds an item creator for each layer

        for (var i = 0; i < msg.agent_layers.length; i++) {
            var lay = msg.agent_layers[i];
          
        }

         for (var i = 0; i < msg.net_layers.length; i++) {
            var lay = msg.net_layers[i];
            var maplayer = L.layerGroup();
            
         }
        
        // TODO add markers to feature groups and set bounds to zoom
                
//        var material = new THREE.MeshLambertMaterial();
//        var geometry = new THREE.CircleBufferGeometry(1000, 32);
        
        // create a Mesh containing the geometry and material
     
       // mesh.material.color.set(0xff0000);
        
//        var geometry = new THREE.CylinderGeometry(1000, 1000, 2000, 8);
//        var material = new THREE.MeshBasicMaterial({ color: 0xff0000 });
//        var mesh = new THREE.Mesh(geometry, material);
        
        
        /* var coord = new itowns.Coordinates('EPSG:4326');
        coord.setFromValues(-87.618952, 41.875817);  // Buckingham Fountain
        coord.altitude = 1000;
         
         // position of the mesh
//         var meshCoord = {longitude: -87.6, latitude: 41.5};
//         var meshCoord = coord;
        // meshCoord.altitude = 1000;
       
        // position and orientation of the mesh
//        mesh.position.copy(meshCoord.as(this.view.referenceCrs));
//        mesh.lookAt(new THREE.Vector3(0, 0, 0));
//        mesh.rotateX(Math.PI / 1);
        
 //       mesh.scale.z = 0.01;
        
        // update coordinate of the mesh
//        mesh.updateMatrixWorld();
       
//        this.group.add(mesh);
        
//        this.view.mesh = mesh;
        
        var featureType = itowns.FEATURE_TYPES.POINT;
        var crs = 'EPSG:4326';
        var options = {buildExtent: true };
        
        var feature = new itowns.Feature(featureType, crs, options);
        var geometry = feature.bindNewGeometry();
        
		
		/* geometry.properties.style = new itowns.Style({
          fill: 'red',
          fillOpacity: 0.5,
          stroke: 'white',
		  'point.color': 'green',
		  'point.line': 'gray',
		  'point.radius': 10,
		  'point.opacity': 1.0
      });
		
		geometry.startSubGeometry(1);
        geometry.pushCoordinates(coord);
        feature.updateExtent(geometry);
        
        var features = new itowns.FeatureCollection(crs, {buildExtent: true });
        
        features.pushFeature(feature);
        features.updateExtent();
        
        this.geometryLayer.source.parsedData = features; 
        
		this.geometryLayer.object3d.updateMatrixWorld();
		this.view.notifyChange(this.geometryLayer);
		//this.geometryLayer.update(); */
		
//        console.log(this.geometryLayer);
//        console.log(this.view);
        
//        this.view.controls.setTilt(60, true);
       
       
	   
//	   console.log(this.marne.object3d);
    }
   


    update(msg) {
        console.log(msg);
        
        var features = [];
        
        let x = -87.6189 + 0.05* Math.random();
        let y = 41.8758 + 0.05* Math.random();
        
        var feature = {}
        feature ["type"] = "Feature";
        feature ["geometry"] = {"type" : "Point", "coordinates" : [x,y]};
        feature ["properties"] = {"name" : "MyAgent"};
        
        features.push(feature);
        
        var feature_collection = {"type" : "FeatureCollection",
        		"features" : features};
        
        
//        agent_layer._layers = [];
//        
//        agent_layer._processData(feature_collection); 
        
         //this.view.mesh.updateMatrixWorld();
        
        for (var i = 0; i < msg.agent_layers.length; i++) {
            var lay = msg.agent_layers[i];
 //           var mapLayer = this.mapLayers.get(lay.layer_id);
 //           var shapeCreator = this.itemCreators.get(lay.layer_id);
            
            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                var add_data = lay.data.add[j];
//                var item = shapeCreator.create(add_data);
  
//                item.addTo(mapLayer);
                
                // TODO Label
                
 //               item.bindPopup('marker ' + add_data.id);
            
//                this.objectMap.set(add_data.id, item);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
                var id = lay.data.remove[j];
//                var item = this.objectMap.get(id);
                
//                item.removeFrom(mapLayer);
//                this.objectMap.delete(id);
//                item = null;
            }

            // Display update existing objects styles
            for (var j = 0; j < lay.data.update.length; j++) {
                var data = lay.data.update[j];
//                var item = this.objectMap.get(data.id);
               
//                shapeCreator.updateStyle(item, data);
            }
            
            // Display update existing objects locations
            for (var j = 0; j < lay.data.move.length; j++) {
                var data = lay.data.move[j];
//                var item = this.objectMap.get(data.id);
               
//                shapeCreator.updateLocation(item, data);
            }
        }
        
        for (var i = 0; i < msg.net_layers.length; i++) {
            var lay = msg.net_layers[i];
            
//            var mapLayer = this.mapLayers.get(lay.layer_id);
            
            //let creator = this.layers.get(lay.layer_id);

            // Display update add new objects
            for (var j = 0; j < lay.data.add.length; j++) {
                var add_data = lay.data.add[j];
                
                // TODO creator
                
//                var line = L.polyline([add_data.source,add_data.target], {});
                    
//                line.addTo(mapLayer);
                          
//                line.setStyle({
//                    color: add_data.color,
//                   weight: add_data.size
//                });
            
                // TODO use different map for network objects
//                this.objectMap.set(add_data.id, line);
            }

            // Display update remove objects
            for (var j = 0; j < lay.data.remove.length; j++) {
//                var id = lay.data.remove[j];
//                var item = this.objectMap.get(id);
                
//                item.removeFrom(mapLayer);
//                this.objectMap.delete(id);
//                item = null;
            }

            // Display update existing objects
            for (var j = 0; j < lay.data.update.length; j++) {
//                var data = lay.data.update[j];
//                var line = this.objectMap.get(data.id);
              
                // TODO styling
              
//                line.setLatLngs([data.source, data.target]);
                
//                line.setStyle({
//                    color: data.color,
//                    weight: data.size                    
//                 });
            }
        }
    }

    destroy() {
//        this.map.remove();
//        this.map = null;
        
    	this.world.destroy();
    	this.world = null;
        
        empty(this.tab);
        empty(this.tab_content);
        document.getElementById("display-tabs").removeChild(this.tab);
        document.getElementById("display-content").removeChild(this.tab_content);
        
        window.removeEventListener('resize', this.windowResize.bind(this));
    }

    checkResize() {
        if (this.resize) {
            this.windowResize();
            this.resize = false;
        }
    }

    // this can get called alot!
    windowResize() {
     if (this.container.clientHeight == 0) {
            this.resize = true;
        } else {
//            this.view.mainLoop.gfxEngine.onWindowResize(this.container.clientWidth, this.container.clientHeight);
//            this.view.camera.resize(this.container.clientWidth, this.container.clientHeight);
//            this.view.notifyChange(this.view.camera.camera3D);
        }
    }

    // Hack to force tile updates when moving in VR
    _onKeyDown = (event) => {
        if (event.repeat) { return; }

       
//        buildingTileLayer._calculateLOD();
      };
}
function onSelectStart( event ) {
	var controller = event.target;
	var intersections = getIntersections( controller );
	if ( intersections.length > 0 ) {
		var intersection = intersections[ 0 ];
		tempMatrix.getInverse( controller.matrixWorld );
		var object = intersection.object;
		object.matrix.premultiply( tempMatrix );
		object.matrix.decompose( object.position, object.quaternion, object.scale );
		object.material.emissive.b = 1;
		controller.add( object );
		controller.userData.selected = object;
	}
}
function onSelectEnd( event ) {
	var controller = event.target;
	if ( controller.userData.selected !== undefined ) {
		var object = controller.userData.selected;
		object.matrix.premultiply( controller.matrixWorld );
		object.matrix.decompose( object.position, object.quaternion, object.scale );
		object.material.emissive.b = 0;
		world._engine._scene.add( object );
		controller.userData.selected = undefined;
	}
}
function getIntersections( controller ) {
	tempMatrix.identity().extractRotation( controller.matrixWorld );
	raycaster.ray.origin.setFromMatrixPosition( controller.matrixWorld );
	raycaster.ray.direction.set( 0, 0, - 1 ).applyMatrix4( tempMatrix );
	return raycaster.intersectObjects( world._engine._scene.children );
}
function intersectObjects( controller ) {
	// Do not highlight when already selected
	if ( controller.userData.selected !== undefined ) return;
	var line = controller.getObjectByName( 'line' );
	var intersections = getIntersections( controller );
	if ( intersections.length > 0 ) {
		var intersection = intersections[ 0 ];
		var object = intersection.object;
		object.material.emissive.r = 1;
		intersected.push( object );
		line.scale.z = intersection.distance;
	} else {
		line.scale.z = 5;
	}
}
function cleanIntersected() {
	while ( intersected.length ) {
		var object = intersected.pop();
		object.material.emissive.r = 0;
	}
}

