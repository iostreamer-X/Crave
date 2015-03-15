/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import java.io.DataInputStream
import java.net.ServerSocket
import labs.amethyst.CraveCore._
import akka.actor.Actor


/*
* The server class listens for incoming double values through a TCP connection.
*/
class Server extends Actor{
    def receive={
        case "host"=>
            try{
                val serverSocket = new ServerSocket(2148)
                val server = serverSocket.accept()
                val inStream = new DataInputStream(server.getInputStream)
                val inData=inStream.readUTF()
                val time=java.lang.Double.parseDouble(inData)
                inStream.close()
                server.close()
                serverSocket.close()
                bufferDataSet ! time
                self ! "host"
            }
            catch{
                case e:Exception =>
                    println(e)
                    self ! "host"
            }

        case _ =>
    }
}
