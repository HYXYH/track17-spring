package track.lections.lection3oop;

/**
 *
 */
public class ImageServer extends AServer {
    @Override
    protected boolean validate(String[] params) {
        return params.length > 0 && params[0].equals("imageId");
    }

    @Override
    protected void processContent() {
        // Загрузим картинку и отдадим ее пользователю
    }

    @Override
    protected String onError() {
        return "Failed to parse ImageId";
    }

    @Override
    public String toString() {
        return "ImageServer";
    }
}

