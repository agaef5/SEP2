package shared;

public class Request
{
  private String handler;
  private String action;
  private Object payload;

  public Request()
  {}
  // Constructor
  public Request(String handler, String action, Object payload) {
    this.handler = handler;
    this.action = action;
    this.payload = payload;
  }

  // Getters
  public String getHandler() {
    return handler;
  }

  public String getAction() {
    return action;
  }

  public Object getPayload() {
    return payload;
  }

  // Setters (optional)
  public void setHandler(String handler) {
    this.handler = handler;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }
}

// handler: auth
//action : login, register