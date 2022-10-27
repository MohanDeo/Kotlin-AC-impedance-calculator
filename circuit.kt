package deo.mohan.test

import org.kotlinmath.Complex
import org.kotlinmath.complex
import org.kotlinmath.pow
open class Circuit : Component{
//open class Circuit(tempComponentContainerPair: Array<Pair<Component, Int>>, circuitNameInput: String) : Component() {

    private var componentContainerPair: ArrayList<Pair<Component, Int>> = arrayListOf()//ArrayList<Pair<Component, Int>>()// =  tempComponentContainerPair
    private var circuitName: String = ""// = circuitNameInput
    constructor()
    constructor(tempComponentContainerPair: ArrayList<Pair<Component, Int>>, circuitNameInput: String){
        this.componentContainerPair = tempComponentContainerPair
        this.circuitName = circuitNameInput
    }
    override fun getComponentName(): String {
        return "Cannot call this function on a circuit object"
    }
    override fun getComponentInfo() {
        return println("Cannot call this function on a circuit object")
    }
    override fun getImpedance(frequencyOfCircuit: Double): Complex {
        val circuitContainerHierarchyForInput = makeCircuitContainerHierarchy()
        val initialTempImpedance = complex(0,0)
        return getCircuitImpedance(circuitContainerHierarchyForInput, initialTempImpedance, frequencyOfCircuit,
            circuitContainerHierarchyForInput.size - 1)
    }

    fun getCircuitImpedance(circuitContainerHierarchy: ArrayList<Pair<Component, Int>>, tempImpedance: Complex, circuitFrequency: Double, indexInput: Int): Complex{
        var newTempImpedance: Complex = complex(0,0)
        val firstIndexInput: Int = circuitContainerHierarchy.size - 1
        var tempImpedanceVar = tempImpedance
        if(indexInput == firstIndexInput){
            tempImpedanceVar += circuitContainerHierarchy[indexInput].first.getImpedance(circuitFrequency)
        }

        if(indexInput==0){
            //val newTempImpedance = tempImpedance
            return tempImpedanceVar
        }
        if(circuitContainerHierarchy[indexInput].second == circuitContainerHierarchy[indexInput - 1].second){
            //calculate impedance in series
            val temp = circuitContainerHierarchy[indexInput - 1].first.getImpedance(circuitFrequency)
            newTempImpedance = temp + tempImpedanceVar
            val impedance = getCircuitImpedance(circuitContainerHierarchy, newTempImpedance, circuitFrequency, indexInput - 1)
            return impedance
        } else{
            //calculate impedance in parallel
            val newTempImpedanceReciprocal = circuitContainerHierarchy[indexInput - 1].first.getImpedance(circuitFrequency).reciprocalOfComplexNumbers() + tempImpedanceVar.reciprocalOfComplexNumbers()
            newTempImpedance = newTempImpedanceReciprocal.reciprocalOfComplexNumbers()
            return getCircuitImpedance(circuitContainerHierarchy, newTempImpedance, circuitFrequency, indexInput - 1)
        }
    }

    fun makeCircuitContainerHierarchy(): ArrayList<Pair<Component, Int>>{
        val circuitContainerHierarchy = ArrayList<Pair<Component, Int>>()
        for(i in 0 until componentContainerPair.size){

            if(i==0){
                circuitContainerHierarchy.add(Pair(componentContainerPair[i].first, 0))

            }else{
                val value = componentContainerPair[i].second + circuitContainerHierarchy[i - 1].second
                circuitContainerHierarchy.add(Pair(componentContainerPair[i].first, value))
            }

        }

        return circuitContainerHierarchy
    }

    open fun getCircuitName(): String{
        return circuitName
    }

    fun getComponentsWithConnTypes(): ArrayList<Pair<Component, Int>>{
        return componentContainerPair
    }

    open fun printImpedanceInfoForComponentsInCircuit(circuitFrequency: Double){
        for(pair in componentContainerPair){
            println(pair.first.getComponentName())
            pair.first.getComponentInfo()
            val componentImpedance: Complex = pair.first.getImpedance(circuitFrequency)
            println("Impedance: ")
            print(componentImpedance)
            println("") //for formatting
        }
    }

    fun printCircuitDiagram(circuitHierarchy: ArrayList<Pair<Component, Int>>){
        val resistorString: String = "Ideal Resistor"
        val capacitorString: String = "Ideal Capacitor"
        val inductorString: String = "Ideal Inductor"
        val nonIdealResistorString: String = "Non ideal resistor"
        val nonIdealCapacitorString:String = "Non ideal capacitor"
        val nonIdealInductorString:String = "Non ideal inductor"

        for(pair in circuitHierarchy){
            val componentName: String = pair.first.getComponentName()
            val componentLetter: String = when(componentName){
                resistorString -> "R"
                capacitorString -> "C"
                inductorString -> "I"
                nonIdealResistorString -> "R!"
                nonIdealCapacitorString -> "C!"
                nonIdealInductorString -> "I!"
                else -> "Not a recognised component"
            }
            println(componentLetter)

            val numberOfDashes = pair.second
            for(i: Int in 0..numberOfDashes){
                print(numberOfDashes)
            }
        }

        fun printCircuitComponents(){
            println("This circuit contains ${componentContainerPair.size} components")

            for(pair in componentContainerPair){
                println(pair.first.getComponentName())
                pair.first.getComponentInfo()
                println() //formatting
            }
        }
    }



}
class TwoCircuit : Circuit{
//class twoCircuit(circuit1: Circuit, circuit2: Circuit, twoCircuitCircuitNameInput: String) : Circuit(tempComponentContainerPair = circuit1.getComponentsWithConnTypes() + circuit2.getComponentsWithConnTypes(), circuitNameInput = twoCircuitCircuitNameInput){
    private var twoCircuit2dVectorOfPairs: List<Circuit> = ArrayList<Circuit>()// = arrayOf(circuit1, circuit2)
    private var twoCircuitName: String = ""// = twoCircuitCircuitNameInput

    constructor(circuit1: Circuit, circuit2: Circuit, twoCircuitCircuitNameInput: String){
        this.twoCircuit2dVectorOfPairs = arrayListOf(circuit1, circuit2)
        this.twoCircuitName = twoCircuitCircuitNameInput
    }

    //construct a TwoCircuit from an existing TwoCircuit and one Circuit
    constructor(twoCircuitInput: TwoCircuit, circuitToBeAdded: Circuit, newTwoCircuitName: String){
        val twoCircuitInputDummyArray: ArrayList<Circuit> = ArrayList()
        for(aCircuit in twoCircuitInput.twoCircuit2dVectorOfPairs){
            twoCircuitInputDummyArray.add(aCircuit)
        }
        val circuitToBeAddedDummyArray: ArrayList<Circuit> = arrayListOf(circuitToBeAdded)
        val wholeDummyArray: List<Circuit> = twoCircuitInputDummyArray + circuitToBeAddedDummyArray

        this.twoCircuit2dVectorOfPairs = wholeDummyArray
        this.twoCircuitName = newTwoCircuitName
    }

    fun getTwoCircuitImpedance(circuitFrequency: Double):Complex{
        var twoCircuitTempImpedance: Complex = complex(0, 0)

        for(index in twoCircuit2dVectorOfPairs.indices){

            val twoCircuitSectionHierarchy: ArrayList<Pair<Component,Int>> = twoCircuit2dVectorOfPairs[index].makeCircuitContainerHierarchy()
            val twoCircuitDummyImpedance:Complex = complex(0, 0)
            val indexOfMax: Int = twoCircuitSectionHierarchy.size

            //+= as circuits added in series
            twoCircuitTempImpedance += getCircuitImpedance(twoCircuitSectionHierarchy, twoCircuitDummyImpedance, circuitFrequency, indexOfMax - 1)//max value lies at the last index
        }
        return twoCircuitTempImpedance
    }

    override fun getCircuitName(): String{
        return twoCircuitName
    }

    override fun printImpedanceInfoForComponentsInCircuit(circuitFrequency: Double) {
        for(index in twoCircuit2dVectorOfPairs.indices){
            twoCircuit2dVectorOfPairs[index].printImpedanceInfoForComponentsInCircuit((circuitFrequency))
        }
    }

    fun printTwoCircuit(){
        val numberOfCircuits = twoCircuit2dVectorOfPairs.size
        println("This nested circuit consists of $numberOfCircuits circuits")

        for(index: Int in twoCircuit2dVectorOfPairs.indices){
            println(" For  circuit ${index+1}: ${twoCircuit2dVectorOfPairs[index].getCircuitName()} ")
            println() //formatting
            println(twoCircuit2dVectorOfPairs[index])
            val circuitHierarchy = twoCircuit2dVectorOfPairs[index].makeCircuitContainerHierarchy()
            printCircuitDiagram(circuitHierarchy)
            println() //formatting
        }
    }
}