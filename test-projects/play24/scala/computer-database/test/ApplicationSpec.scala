import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api._
import play.api.i18n._
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  
  import models._

  // -- Date helpers
  
  def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
  
  // -- i18n

  val conf = Configuration.reference
  val messages = new DefaultMessagesApi(Environment.simple(), conf, new DefaultLangs(conf))

  // --
  
  "Application" should {
    
    "redirect to the computer list on /" in {
      
      val result = new controllers.Application(messages).index(FakeRequest())
      
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome.which(_ == "/computers")
      
    }
    
    "list computers on the the first page" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val result = new controllers.Application(messages).list(0, 2, "")(FakeRequest())

        status(result) must equalTo(OK)
        contentAsString(result) must contain("574 computers found")
        
      }      
    }
    
    "filter computer by name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val result = new controllers.Application(messages).list(0, 2, "Apple")(FakeRequest())

        status(result) must equalTo(OK)
        contentAsString(result) must contain("13 computers found")
        
      }      
    }
    
    "create new computer" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val badResult = new controllers.Application(messages).save(FakeRequest())
        
        status(badResult) must equalTo(BAD_REQUEST)
        
        val badDateFormat = new controllers.Application(messages).save(
          FakeRequest().withFormUrlEncodedBody("name" -> "FooBar", "introduced" -> "badbadbad", "company" -> "1")
        )
        
        status(badDateFormat) must equalTo(BAD_REQUEST)
        contentAsString(badDateFormat) must contain("""<option value="1" selected="selected">Apple Inc.</option>""")
        contentAsString(badDateFormat) must contain("""<input type="text" id="introduced" name="introduced" value="badbadbad" />""")
        contentAsString(badDateFormat) must contain("""<input type="text" id="name" name="name" value="FooBar" />""")
        
        val result = new controllers.Application(messages).save(
          FakeRequest().withFormUrlEncodedBody("name" -> "FooBar", "introduced" -> "2011-12-24", "company" -> "1")
        )
        
        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_ == "/computers")
        flash(result).get("success") must beSome.which(_ == "Computer FooBar has been created")
        
        val list = new controllers.Application(messages).list(0, 2, "FooBar")(FakeRequest())

        status(list) must equalTo(OK)
        contentAsString(list) must contain("One computer found")
        
      }
    }
    
  }
  
}
