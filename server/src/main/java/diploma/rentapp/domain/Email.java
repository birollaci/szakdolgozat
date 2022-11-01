package diploma.rentapp.domain;

public class Email {
    
    private String target;
    private String subject;
    private String body;

    public Email(){}

    public Email(String target, String subject, String body) {
        this.target = target;
        this.subject = subject;
        this.body = body;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
