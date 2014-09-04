
package eu.ecim.parking.converters

import groovy.json.JsonSlurper

/**
 *
 * @author stzoannos
 */

class BeParkConverter implements TemplateConverter {
    
    @Override
    public  String convert(String json) {
        
        println 'starting conversion'
       
        def builder = new groovy.json.JsonBuilder()
       
        def jsonObjFromBePark = new JsonSlurper().parseText(json)
        
        def parkings = jsonObjFromBePark.result.parkings
                
        def root = builder.dataset {
                
            author {
                id "relational"
                value "Relational"
            }
                
            poi parkings.collect { parking->
                  
                [id: "BePark_" + parking.id,
                    title: parking.name,
                    description: "",
                    category: ["offStreetParking"],
                    location: {
                        point {
                            pos {
                                posList  """$parking.coordinate.latitude $parking.coordinate.longitude"""
                            }
                        }
                        address {
                            value """$parking.address.street $parking.address.number"""
                            postal parking.address.zip
                            city parking.address.city
                        
                        }
                    },
                   
                    attribute: [{
                        
                            term "URL"
                            type "url"
                            text parking.url
                            tplIdentifier "#Citadel_website" 
                        
                        } , 
                        {
                            term "Price:"
                            type "price"
                            text """minimum: $parking.price.length-$parking.price.price, 
                                    overtime: $parking.price.overtime_length-$parking.price.overtime_price,  
                                    subscription: $parking.price.sub_length-$parking.price.sub_price, 
                                    oneshot: $parking.price.oneshot_price"""
                        },
                        
                        {
                            term "Park Type"
                            type "park type"
                            text 'underground'
                            tplIdentifier "#Citadel_parkType" 
                        }
                    ]
                ]
                   
            }
            
        }

        return builder.toPrettyString();
    
        
    }
}

