
package eu.ecim.parking.converters

import groovy.json.JsonSlurper

/**
 *
 * @author stzoannos
 */

class PoisConverter implements TemplateConverter {
    
    @Override
    public  String convert(String json) {
        
        println 'starting conversion'
       
        def builder = new groovy.json.JsonBuilder()
       
        def jsonObj = new JsonSlurper().parseText(json)
        
        def parkings = jsonObj
                
        def root = builder.dataset {
                
            author {
                id "relational"
                value "Relational"
            }
                
            poi parkings.collect { parking->
                  
                [id: "id_taxi:" + parking.recordid,
                    title: parking.fields.nom,
                    description: "not available",
                    category: ["taxis"],
                    location: {
                        point {
                            pos {
                                posList  parking.geometry.coordinates.get(1) + " " + parking.geometry.coordinates.get(0)
                                
                            }
                        }
                        address {
                            value """$parking.fields.adres_fr"""
                           
                            city "Brussels"
                        
                        }
                    },
                   
                    attribute: [{
                        
                            term "taxi places"
                            type "taxi places"
                            text parking.fields.places
                            tplIdentifier "#Citadel_taxi_places" 
                        
                        } , 
                        {
                            term "Park Type"
                            type "park type"
                            text 'taxi stand'
                            tplIdentifier "#Citadel_parkType" 
                        }
                        
                    ]
                ]
                   
            }
            
        }

        return builder.toPrettyString();
    
        
    }
}

