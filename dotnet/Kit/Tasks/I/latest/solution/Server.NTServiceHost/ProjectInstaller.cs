using System.ComponentModel;
using System.Configuration.Install;

namespace PPWCode.Kit.Tasks.Server.NTServiceHost
{
    [RunInstaller(true)]
    public partial class ProjectInstaller : Installer
    {
        public ProjectInstaller()
        {
            InitializeComponent();
        }
    }
}
