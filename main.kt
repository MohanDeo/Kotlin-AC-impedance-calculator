package deo.mohan.test

import org.kotlinmath.Complex
import org.kotlinmath.complex
import org.kotlinmath.pow

fun main() {
    var userConstructedCircuits: List<Circuit> = listOf()
    var userConstructedTwoCircuits: List<TwoCircuit> = listOf()
    var userConstructedComponents: List<Component> = listOf()

    userConstructedComponents = userConstructedComponents + IdealInductor(3.2)
    userConstructedComponents = userConstructedComponents + NonIdealResistor(7.0, 0.00000000005, 2.0)
    userConstructedComponents = userConstructedComponents + IdealResistor(3.0)
    userConstructedComponents = userConstructedComponents + NonIdealCapacitor(0.000000000006, 0.004, 0.0003)

    var tempComponentContainerPairDemo1: List<Pair<Component, Int>> = listOf()
    tempComponentContainerPairDemo1 = tempComponentContainerPairDemo1 + Pair(userConstructedComponents[0], 0)
    tempComponentContainerPairDemo1 = tempComponentContainerPairDemo1 + Pair(userConstructedComponents[1], 1)
    tempComponentContainerPairDemo1 = tempComponentContainerPairDemo1 + Pair(userConstructedComponents[0], 1)
    tempComponentContainerPairDemo1 = tempComponentContainerPairDemo1 + Pair(userConstructedComponents[3], 0)
    userConstructedCircuits = userConstructedCircuits + Circuit(tempComponentContainerPairDemo1 as ArrayList<Pair<Component, Int>>, "Test1")

    var tempComponentContainerPairDemo2: List<Pair<Component, Int>> = listOf()
    tempComponentContainerPairDemo2 = tempComponentContainerPairDemo2 + Pair(userConstructedComponents[3], 0)
    tempComponentContainerPairDemo2 = tempComponentContainerPairDemo2 + Pair(userConstructedComponents[1], 1)
    tempComponentContainerPairDemo2 = tempComponentContainerPairDemo2 + Pair(userConstructedComponents[2], 0)
    tempComponentContainerPairDemo2 = tempComponentContainerPairDemo2 + Pair(userConstructedComponents[0], 1)

    userConstructedCircuits = userConstructedCircuits + Circuit(tempComponentContainerPairDemo2 as ArrayList<Pair<Component, Int>>, "Test2")
    userConstructedTwoCircuits = userConstructedTwoCircuits + TwoCircuit(userConstructedCircuits[0], userConstructedCircuits[1],
        "Test 1 and Test 2")

    userConstructedTwoCircuits = userConstructedTwoCircuits + TwoCircuit(userConstructedTwoCircuits[0], userConstructedCircuits[0],
        "Test 1 and Test 2 with Test 1 again")

    //print(userConstructedCircuits[0].getComponentsWithConnTypes())//weird component names
    println(userConstructedCircuits[0].getImpedance(52390.5))
    //println(userConstructedCircuits[0].printImpedanceInfoForComponentsInCircuit(52390.5))
    val complexTest:Complex = complex(-1, 2)
    println("Complex, ${complexTest.reciprocalOfComplexNumbers()}")
    println(pow(-1, complexTest))
}