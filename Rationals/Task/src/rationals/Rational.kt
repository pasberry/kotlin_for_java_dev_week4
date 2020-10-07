package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

class RationalRange(override val start:Rational, override val endInclusive: Rational) : ClosedRange<Rational>


class Rational(val numerator:BigInteger, val denominator:BigInteger) : Comparable<Rational> {

    init {
        if ( denominator == BigInteger.ZERO ) {
            throw IllegalArgumentException()
        }

    }

    override fun compareTo(other: Rational): Int {

        val selfValue = this.numerator.toFloat() / this.denominator.toFloat()
        val otherValue = other.numerator.toFloat() / other.denominator.toFloat()

        return when {
            (selfValue - otherValue) > 0 -> 1
            (selfValue - otherValue) < 0 -> -1
            else -> 0
        }
    }

    override fun toString(): String {

        val numeratorResult = if (numerator < BigInteger.ONE && denominator < BigInteger.ZERO ) {
           numerator.abs()
        }
        else if(numerator > BigInteger.ONE && denominator < BigInteger.ZERO){
            -numerator
        }
        else {
            numerator
        }

        val denominatorResult = if (numerator < BigInteger.ONE && denominator < BigInteger.ZERO ) {
            denominator.abs()
        }else if(numerator > BigInteger.ONE && denominator < BigInteger.ZERO){
            denominator.abs()
        }
        else {
            denominator
        }

        return when {
            !numeratorResult.equals(BigInteger.ONE) -> {
                val gcd = numeratorResult.gcd(denominatorResult)

                val numeratorReduced = numeratorResult / gcd
                val denominatorReduced = denominatorResult / gcd

                val result = if (denominatorReduced.equals(BigInteger.ONE) ) {
                    "$numeratorReduced"
                }else {
                    "$numeratorReduced/$denominatorReduced"
                }

                result
            }
            else -> numeratorResult.toString()
        }
    }

    override fun equals(any: Any?): Boolean {

        val other:Rational? = any as? Rational

        var numeratorA = BigInteger.ZERO
        var numeratorB = BigInteger.ZERO

        var denominatorA = BigInteger.ZERO
        var denominatorB = BigInteger.ZERO

        other.takeUnless { it == null }
                ?.apply {
                    val gcdA = this@Rational.numerator.gcd(this@Rational.denominator)

                    denominatorA = this@Rational.denominator / gcdA
                    numeratorA = this@Rational.numerator / gcdA

                    val gcdB = this.numerator.gcd(this.denominator)

                    denominatorB = this.denominator / gcdB
                    numeratorB = this.numerator / gcdB


                }

        if (numeratorA < BigInteger.ONE && denominatorA < BigInteger.ZERO ) {
            numeratorA = numeratorA.abs()
            denominatorA = denominatorA.abs()
        }else if (numeratorA > BigInteger.ONE && denominatorA < BigInteger.ZERO ){
            numeratorA = -numeratorA
            denominatorA = denominatorA.abs()
        }

        if (numeratorB < BigInteger.ONE && denominatorB < BigInteger.ZERO ) {
            numeratorB = numeratorB.abs()
            denominatorB = denominatorB.abs()
        }else if (numeratorB > BigInteger.ONE && denominatorB < BigInteger.ZERO ) {
            numeratorB = -numeratorB
            denominatorB = denominatorB.abs()
        }

       return when {
           other == null -> false
           (denominatorA == denominatorB) && (numeratorA == numeratorB) -> true
           else -> false
       }
    }
}

operator fun Rational.unaryMinus():Rational = Rational(-this.numerator,this.denominator)

operator fun Rational.plus(other:Rational):Rational {

    var denominatorResult:BigInteger = this.denominator
    var numeratorResult:BigInteger = this.numerator

    when {
        this.denominator == other.denominator -> {

            denominatorResult = this.denominator
            numeratorResult = this.numerator + other.numerator

        }
        this.denominator != other.denominator -> {

            denominatorResult = this.denominator * other.denominator
            numeratorResult = (this.numerator * other.denominator) + (other.numerator * this.denominator)
        }
    }

    return Rational(numeratorResult, denominatorResult)
}

operator fun Rational.minus(other:Rational):Rational {

    var denominatorResult:BigInteger = this.denominator
    var numeratorResult:BigInteger = this.numerator

    when {
        this.denominator == other.denominator -> {

            denominatorResult = this.denominator
            numeratorResult = this.numerator - other.numerator

        }
        this.denominator != other.denominator -> {

            denominatorResult = this.denominator * other.denominator
            numeratorResult = (this.numerator * other.denominator) - (other.numerator * this.denominator)
        }
    }

    return Rational(numeratorResult, denominatorResult)
}

operator fun Rational.times(other: Rational) :Rational {

    return Rational(numerator * other.numerator, denominator * other.denominator)

}

operator fun Rational.div(other: Rational): Rational {

    return Rational(numerator * other.denominator, denominator * other.numerator)

}
operator fun Rational.rangeTo(other: Rational) : ClosedRange<Rational> {

    return RationalRange(this, Rational(other.numerator, other.denominator))
}


infix fun Int.divBy(other: Int) : Rational = Rational(this.toBigInteger(), other.toBigInteger() )

infix fun Long.divBy(other: Long) : Rational = Rational(this.toBigInteger(), other.toBigInteger() )

infix fun BigInteger.divBy(other: BigInteger) : Rational = Rational(this, other )

fun String.toRational() : Rational {

    return when {

        contains("/") -> {
            val tokens = split("/")
             Rational(tokens[0].toBigInteger(), tokens[1].toBigInteger())
        }
        else -> {
             Rational(toBigInteger(), BigInteger.ONE)
        }
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}