/*<license>
Copyright 2011 - $Date: 2008-11-06 15:27:53 +0100 (Thu, 06 Nov 2008) $ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

#region Using

using System.Runtime.CompilerServices;

#endregion

namespace PPWCode.Value.I.Time.Interval
{
    /// <summary>
    /// <para>Time intervals are dangerously and treacherously difficult to reason about.
    /// <em>Experience shows that falling back to the begin and end dates as separated dates and
    /// reasoning about them does not solve the complexity, and results in even more difficult
    /// reasonings and is even more error prone.</em></para>
    /// <para><see cref="ITimeInterval"/> is an interface where we define
    /// the general rules for time intervals in our approach. This package offers several effective
    /// implementations, that have different constraints that they adhere to. For each of these
    /// implementations, persistence support and other translation support is offered.</para>
    /// <para>Reasoning about time intervals should be done with <em>Allen's Interval Algebra</em>. 
    /// This is implemented in <see cref="TimeIntervalRelation"/>. To reason
    /// about the relation between time intervals and points in time, please use
    /// <see cref="TimePointIntervalRelation"/>.</para>
    /// </summary>
    [CompilerGenerated]
    internal class NamespaceDoc
    {
    }
}