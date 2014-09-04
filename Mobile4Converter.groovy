
import eu.ecim.parking.converters.TemplateConverter

/**
 *
 * @author stzoannos
 */
class Mobile4Converter implements TemplateConverter {
    
    
    
    @Override
    public  String convert(String json) {
        
        
        println 'starting conversion for mobile4'
        
        List p = []
        
        json.splitEachLine(";") {fields ->
                    
            def zone = fields.get(0)
            def roadName = fields.get(4)
            def lat = fields.get(5)
            def lon = fields.get(6)
                    
            p << new ParkingSpot(zone: fields.get(1), id: fields.get(0), title:fields.get(3), roadName: fields.get(4), lat: fields.get(5), lon: fields.get(6))                    
                    
        }
        
        
        def builder = new groovy.json.JsonBuilder()
                
        
        def root = builder.dataset {
                        
            author {
                id "relational"
                value "relational"
            }
                    
            //            def zone = fields.get(1)
            //            def roadName = fields.get(4)
            //            def lat = fields.get(5)
            //            def lon = fields.get(6)
                        
            poi p.collect { parking ->
                          
                [id: "Mobile4_" + parking.id,
                    title: parking.title,
                    description: "not available",
                    category: ["onStreetParking"],
                    location: {
                        point {
                            pos {
                                posList "${parking.lat} ${parking.lon}"
                                //                                posList "37.984287 23.767942"
                            }
                        }
                        address {
                            value parking.roadName
                            postal "1030"
                            city "Brussels"
                                
                        }
                    },
                    
                    
                    
                    attribute: [ ]
                    
                           
                ]
                           
            }
                    
        }
        
        //        println builder.toPrettyString();
                
        return builder.toPrettyString();
        

    }
}

 
class ParkingSpot {
    String zone
    String id
    String title
    String roadName
    String lat
    String lon
}

    

