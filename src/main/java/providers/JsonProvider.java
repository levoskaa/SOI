package providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
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
import com.google.protobuf.util.JsonFormat;

@Provider
@Consumes("application/json")
@Produces("application/json")
public class JsonProvider
	implements MessageBodyReader<Message>, MessageBodyWriter<Message> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType) {
	return true;
    }

    @Override
    public Message readFrom(Class<Message> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType,
	    MultivaluedMap<String, String> httpHeaders,
	    InputStream entityStream)
	    throws IOException, WebApplicationException {
	try {
	    Message.Builder builder = (Message.Builder) type
		    .getMethod("newBuilder").invoke(type);
	    JsonFormat.parser().merge(new InputStreamReader(entityStream),
		    builder);
	    return builder.build();
	} catch (Exception e) {
	    return null;
	}
    }

    @Override
    public long getSize(Message message, Class<?> cls, Type type,
	    Annotation[] annots, MediaType mediaType) {
	return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType) {
	return true;
    }

    @Override
    public void writeTo(Message t, Class<?> type, Type genericType,
	    Annotation[] annotations, MediaType mediaType,
	    MultivaluedMap<String, Object> httpHeaders,
	    OutputStream entityStream)
	    throws IOException, WebApplicationException {
	entityStream.write(JsonFormat.printer().print(t).getBytes());
    }
}
