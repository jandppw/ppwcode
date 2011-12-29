using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Linq;
using System.Text;

namespace PPWCode.Value.I.Time.Interval
{
    /// <summary>
    /// Missing methods for dealing with bit patterns.
    /// To express a bit pattern <strong>a <c><em>uint</em></c> should be used</strong>.
    /// </summary>
    public static class Bitpattern
    {
        /// <summary>
        /// String representation of a <paramref name="bitpattern"/> of
        /// <paramref name="nrOfBits"/> long.
        /// </summary>
        /// <returns>
        /// The bit pattern representing <paramref name="bitpattern"/>
        /// as &quot;0&quot; and &quot;1&quot;. The least significant bit
        /// is on the right. The pattern is padded with &quot;0&quot; on the
        /// left to get <paramref name="nrOfBits"/> characters.
        /// </returns>
        public static string FullBitPattern(this uint bitpattern, int nrOfBits)
        {
            Contract.Requires(nrOfBits > 0);
            Contract.Requires(bitpattern < Math.Pow(2, nrOfBits));

            string bitString = Convert.ToString(bitpattern, 2);
            while (bitString.Length < nrOfBits)
            {
                bitString = "0" + bitString;
            }
            return bitString;
        }

        /// <summary>
        /// Based on Java, Integer.numberOfTrailingZeros.
        /// </summary>
        /// <remarks>
        /// (Based on Java documentation):
        /// Returns the number of zero bits following the lowest-order ("rightmost")
        /// one-bit in the representation of the specified
        /// <c>uint</c> value.  Returns 32 if the specified value has no
        /// one-bits in its representation, in other words if it is
        /// equal to zero.
        /// </remarks>
        public static int NumberOfTrailingZeros(this uint i)
        {
            uint y;
            if (i == 0)
            {
                return 32;
            }
            uint n = 31;
            y = i << 16;
            if (y != 0)
            {
                n = n - 16;
                i = y;
            }
            y = i << 8;
            if (y != 0)
            {
                n = n - 8;
                i = y;
            }
            y = i << 4;
            if (y != 0)
            {
                n = n - 4;
                i = y;
            }
            y = i << 2;
            if (y != 0)
            {
                n = n - 2;
                i = y;
            }
            return (int)(n - ((i << 1) >> 31));
        }

        /// <summary>
        /// A basic relation is expressed by a single bit in the bit pattern.
        /// </summary>
        public static bool IsBasicBitPattern(this uint bitPattern)
        {
            /* http://graphics.stanford.edu/~seander/bithacks.html
             * Determining if an integer is a power of 2
             * unsigned int v; // we want to see if v is a power of 2
             * bool f;         // the result goes here
             * f = (v & (v - 1)) == 0;
             *
             * Note that 0 is incorrectly considered a power of 2 here. To remedy this, use:
             * f = !(v & (v - 1)) && v;
             */
            return ((bitPattern & (bitPattern - 1)) == 0) && (bitPattern != 0);
        }

        /// <summary>
        /// Based on Java, Integer.bitCount.
        /// </summary>
        /// <remarks>
        /// (Based on Java documentation):
        /// Returns the number of one-bits in binary
        /// representation of the specified <c>uint</c> value. This function is
        /// sometimes referred to as the <dfn>population count</dfn>.
        /// </remarks>
        public static int BitCount(this uint i)
        {
            i = i - ((i >> 1) & 0x55555555);
            i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
            i = (i + (i >> 4)) & 0x0f0f0f0f;
            i = i + (i >> 8);
            i = i + (i >> 16);
            return (int)(i & 0x3f);
        }
    }
}
