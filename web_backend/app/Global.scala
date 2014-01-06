import com.escalatesoft.subcut.inject._
import config.Binding
import play.api.GlobalSettings
import scala.reflect._


class Global extends GlobalSettings {

    // TODO: default to injected controller, use match case
    /**
     * Customise instantiating controller so that they support SubCut injection.
     */
    override def getControllerInstance[A](controllerClass: Class[A]): A = {
        try {
            controllerClass.newInstance()
        } catch {
            case reflex: InstantiationException =>
                val injectedConstructor =
                    controllerClass.getDeclaredConstructor(classOf[BindingModule])
                injectedConstructor.newInstance(Binding)
        }
    }
}