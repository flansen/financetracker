package flhan.de.financemanager.login.createjoinhousehold

/**
 * Created by fhansen on 02.10.17.
 */
enum class InputState {
    Join,
    Create
}

data class ViewState (
    var text: String = "",
    var inputState: InputState = InputState.Join
)