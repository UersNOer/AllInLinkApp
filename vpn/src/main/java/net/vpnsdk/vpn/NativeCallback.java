package net.vpnsdk.vpn;

abstract class NativeCallback {
	abstract int onVpnConnectFailed(int error);
	abstract int onVpnConnected();
	abstract int onVpnDisconnected(int error);
	abstract int onVpnConnecting();
	abstract int onVpnDisconnecting();
	abstract int onVpnReconnecting();
	abstract String onVpnLogin(String resource);
	abstract String onVpnDevReg(String resource);
	abstract int onVpnConfigRequest(String configuration);
	abstract int onVpnProtect(int sock);
}