package nostale.handler.interfaces;

import nostale.domain.LoginFailType;

public interface ILoginHandler {
	public void onError(LoginFailType error);
}
