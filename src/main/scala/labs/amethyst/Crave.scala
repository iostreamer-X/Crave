/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import labs.amethyst.CraveCore._

object Crave extends App {
	Terminal.initialize
	val testData = List(9.0, 9.1, 9.1, 9.2, 8.2, 9.3, 8.4, 8.14,19.59,19.58,19.03)
	Terminal.?(println)(23)
	testData.foreach(bufferDataSet !)
}
