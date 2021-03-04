package providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.protobuf.Message;

@Provider
@Consumes("application/x-protobuf")
@Produces("application/x-protobuf")
public class ProtoBufProvider
	implements MessageBodyReader<Message>, MessageBodyWriter<Message> {
    @Override
    public boolean isReadable(Class<?> cls, Type type, Annotation[] annots,
	    MediaType mediaType) {
	return true;
    }

    @Override
    public Message readFrom(Class<Message> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType,
	    MultivaluedMap<String, String> httpHeaders,
	    InputStream entityStream)
	    throws IOException, WebApplicationException {
	try {
	    Method parseFromMethod = type.getMethod("parseFrom",
		    InputStream.class);
	    return (Message) parseFromMethod.invoke(null, entityStream);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public long getSize(Message message, Class<?> cls, Type type,
	    Annotation[] annots, MediaType mediaType) {
	return -1;
    }

    @Override
    public boolean isWriteable(Class<?> cls, Type type, Annotation[] annots,
	    MediaType mediaType) {
	return true;
    }

    @Override
    public void writeTo(Message message, Class<?> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType,
	    MultivaluedMap<String, Object> httpHeaders,
	    OutputStream entityStream)
	    throws IOException, WebApplicationException {
	message.writeTo(entityStream);
    }
}