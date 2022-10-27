package deo.mohan.test
import org.kotlinmath.*


fun Complex.reciprocalOfComplexNumbers(): Complex{
    val realPartOfReciprocal: Double = conj().re / (mod * mod)
    val imPartOfReciprocal: Double = conj().im / (mod * mod)
    return complex(realPartOfReciprocal, imPartOfReciprocal)
}

abstract class Component{
    abstract fun getComponentName(): String
    abstract fun getComponentInfo(): Unit
    abstract fun getImpedance(frequencyOfCircuit: Double): Complex
}

open class IdealResistor(inputResistance: Double) : Component() {
    private val componentName = "Ideal Resistor"
    private val resistance: Double = inputResistance

    override fun getComponentName(): String {
        return componentName
    }
    override fun getComponentInfo(): Unit{
        return println("Resistance: $resistance")
    }
    override fun getImpedance(frequencyOfCircuit: Double): Complex{
        return complex(resistance, 0)
    }
}
open class IdealCapacitor(inputCapacitance: Double) : Component(){
    private val componentName = "Ideal Capacitor"
    private val capacitance: Double = inputCapacitance

    override fun getComponentName(): String {
        return componentName
    }
    override fun getComponentInfo(): Unit {
        return println("Capacitance: $capacitance")
    }
    override fun getImpedance(frequencyOfCircuit: Double): Complex{
        val imPart: Double = frequencyOfCircuit * capacitance
        val reciprocalImpedance: Complex = complex(0, imPart)
        //return pow(-1, reciprocalImpedance)
        return reciprocalImpedance.reciprocalOfComplexNumbers()
    }

}
open class IdealInductor(inputInductance: Double): Component(){
    private val componentName = "Ideal Inductor"
    private val inductance = inputInductance

    override fun getComponentName(): String {
        return componentName
    }
    override fun getComponentInfo(): Unit {
        return println("The inductance is $inductance")
    }
    override fun getImpedance(frequencyOfCircuit: Double): Complex{
        return complex(0.0, frequencyOfCircuit * inductance)
    }

}

class NonIdealResistor(inputNonIdealResistance: Double, inputParasiticInductance: Double, inputParasiticCapacitance: Double) : IdealResistor(inputResistance = inputNonIdealResistance){
    private val componentName = "Non Ideal Resistor"
    private val nonIdealResistance = inputNonIdealResistance
    private val parasiticInductance = inputParasiticInductance
    private val parasiticCapacitance = inputParasiticCapacitance

    override fun getComponentName(): String {
        return componentName
    }
    override fun getComponentInfo(): Unit {
        return println("Resistance: ${nonIdealResistance}, Parasitic Inductance: ${parasiticInductance}, Parasitic Capacitance: ${parasiticCapacitance}")
    }

    override fun getImpedance(frequencyOfCircuit: Double): Complex {
        val nonIdealResistorResistance: Complex =  complex(nonIdealResistance, 0)
        val impedanceRcCircuitDenom: Complex = complex(1, (frequencyOfCircuit * nonIdealResistance * parasiticCapacitance))
        val impedanceRcCircuit: Complex = nonIdealResistorResistance / impedanceRcCircuitDenom
        val impedanceOfInductor: Complex = complex(0, (frequencyOfCircuit * parasiticInductance))
        val nonIdealResistorImpedance: Complex = impedanceOfInductor + impedanceRcCircuit

        return nonIdealResistorImpedance
    }
}
class NonIdealCapacitor(inputNonIdealCapacitance: Double, inputParasiticResistance: Double, inputParasiticInductance: Double) : IdealCapacitor(inputCapacitance = inputNonIdealCapacitance){
    private val componentName = "Non Ideal Capacitor"
    private val nonIdealCapacitance = inputNonIdealCapacitance
    private val parasiticResistance = inputParasiticResistance
    private val parasiticInductance = inputParasiticInductance

    override fun getComponentName(): String {
        return componentName
    }
    override fun getComponentInfo(): Unit {
        return println("Capacitance: ${nonIdealCapacitance}, Parasitic Resistance: ${parasiticResistance}, Parasitic Inductance: ${parasiticInductance}")
    }

    override fun getImpedance(frequencyOfCircuit: Double): Complex {
        val realPart: Double = parasiticResistance
        val imPart: Double = frequencyOfCircuit * parasiticInductance - (1 / (frequencyOfCircuit * nonIdealCapacitance))
        val nonIdealCapacitorImpedance: Complex = complex(realPart, imPart)


        return nonIdealCapacitorImpedance
    }
}
class NonIdealInductor(inputNonIdealInductance: Double, inputParasiticResistance: Double, inputParasiticCapacitance: Double) : IdealInductor(inputInductance = inputNonIdealInductance) {
    private val componentName = "Non Ideal Inductor"
    private val nonIdealInductance = inputNonIdealInductance
    private val parasiticResistance = inputParasiticResistance
    private val parasiticCapacitance = inputParasiticCapacitance

    override fun getComponentName(): String {
        return componentName
    }

    override fun getComponentInfo(): Unit {
        return println("Inductance: ${nonIdealInductance}, Parasitic Resistance: ${parasiticResistance}, Parasitic Capacitance: ${parasiticCapacitance}")
    }

    override fun getImpedance(frequencyOfCircuit: Double): Complex {
        val impedance1: Complex = complex(parasiticResistance, (frequencyOfCircuit * nonIdealInductance))
        val impedance2: Complex = complex(0, (frequencyOfCircuit * parasiticCapacitance))
        val totalImpedanceReciprocal: Complex = (impedance1.reciprocalOfComplexNumbers() + impedance2)
        val nonIdealInductorImpedance: Complex = totalImpedanceReciprocal.reciprocalOfComplexNumbers()
        return nonIdealInductorImpedance
    }
}