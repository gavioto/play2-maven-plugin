package controllers.admin

import play.api._
import play.api.mvc._

class Application extends Controller {
  
  def index = Action {
    // Change view package
    Ok(views.html.admin.index("Admin application is ready."))
  }
  
}