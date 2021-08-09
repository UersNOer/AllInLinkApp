/*
 *  Copyright (C) 2012 Array Networks, Inc. All rights reserved.
 *  
 */

package net.vpnsdk.vpn;

interface IVpnService {
   
    int protectSocket(int socket);
    
    int getVpnInterface(String configuration, String host);
    
    void stop();
}
