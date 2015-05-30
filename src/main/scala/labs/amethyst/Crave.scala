/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import labs.amethyst.CraveCore._

import scala.util.Random

object Crave extends App {
    Terminal.initialize
    var testData = List(9.0, 9.1, 9.2, 8.2, 10.11, 10.19, 10.42, 9.3, 8.4, 8.14)
    Terminal.?
    testData.foreach(i => testData = testData.::(i + 12))
    //testData.foreach(bufferDataSet !)
	0.to(100).map(i=>Random.nextDouble()*(i%24)).foreach(bufferDataSet !)
}
