# DistributedSystems

To establish ssh tunnel from local computer to nodes in protected cluster:  
ssh -L <local-port>:localhost:<remote-port> -p <ssh-port> user@remote-host  
So to connect our local 8081 port to the remote port 54321 of our node:  
ssh -L 8081:localhost:54321 -p 2051 root@6dist.idlab.uantwerpen.be
