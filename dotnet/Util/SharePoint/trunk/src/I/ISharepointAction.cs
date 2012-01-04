namespace PPWCode.Util.SharePoint.I
{
    public interface ISharepointAction
    {
        void Execute();
        void Undo();
    }
}