﻿using System.Collections.Generic;
using System.Diagnostics.Contracts;

namespace PPWCode.Vernacular.Persistence.II
{
    [ContractClass(typeof(IPagedListContract<>))]
    public interface IPagedList<T>
    {
        [Pure]
        int PageIndex { get; }

        [Pure]
        int PageSize { get; }

        [Pure]
        int TotalCount { get; }

        [Pure]
        int TotalPages { get; }

        [Pure]
        bool HasPreviousPage { get; }

        [Pure]
        bool HasNextPage { get; }

        IList<T> Items { get; }
    }

    // ReSharper disable once InconsistentNaming
    [ContractClassFor(typeof(IPagedList<>))]
    public abstract class IPagedListContract<T> : IPagedList<T>
    {
        [Pure]
        public int PageIndex
        {
            get
            {
                Contract.Ensures(Contract.Result<int>() > 0);

                return default(int);
            }
        }

        [Pure]
        public int PageSize
        {
            get
            {
                Contract.Ensures(Contract.Result<int>() > 0);

                return default(int);
            }
        }

        [Pure]
        public int TotalCount
        {
            get
            {
                Contract.Ensures(Contract.Result<int>() >= 0);

                return default(int);
            }
        }

        [Pure]
        public int TotalPages
        {
            get
            {
                Contract.Ensures(Contract.Result<int>() == ((TotalCount / PageSize) + (TotalCount % PageSize > 0 ? 1 : 0)));

                return default(int);
            }
        }

        [Pure]
        public bool HasPreviousPage
        {
            get
            {
                Contract.Ensures(Contract.Result<bool>() == (PageIndex > 1));

                return default(bool);
            }
        }

        [Pure]
        public bool HasNextPage
        {
            get
            {
                Contract.Ensures(Contract.Result<bool>() == (PageIndex + 1 < TotalPages));

                return default(bool);
            }
        }

        public IList<T> Items
        {
            get
            {
                Contract.Ensures(Contract.Result<IList<T>>() != null);

                return default(IList<T>);
            }
        }
    }
}