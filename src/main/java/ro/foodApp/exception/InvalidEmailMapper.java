package ro.foodApp.exception;


import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.xml.ws.Response;

//@Provider
public class InvalidEmailMapper //implements ExceptionMapper<InvalidEmailException>
{

//    @Override
//    public Response toResponse(InvalidEmailException ex) {
//        return Response.status(400).entity(ex.getMessage()).type("text/plain")
//                .build();
//    }
}