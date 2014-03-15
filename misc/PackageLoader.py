class classPathHacker(object):
  """Original Author: SG Langer Jan 2007, conversion from Java to Jython
  Updated version (supports Jython 2.5.2) From http://glasblog.1durch0.de/?p=846
  
  Purpose: Allow runtime additions of new Class/jars either from
  local files or URL
  """
  import jarray
  import java.lang.reflect.Method
  import java.io.File
  import java.net.URL
  import java.net.URLClassLoader
    
  def addFile(self, s):
    """Purpose: If adding a file/jar call this first
    with s = path_to_jar"""
    # make a URL out of 's'
    f = self.java.io.File(s)
    u = f.toURL()
    a = self.addURL(u)
    return a
 
  def addURL(self, u):
    """Purpose: Call this with u= URL for
    the new Class/jar to be loaded"""
    sysloader = self.java.lang.ClassLoader.getSystemClassLoader()
    sysclass = self.java.net.URLClassLoader
    method = sysclass.getDeclaredMethod("addURL", [self.java.net.URL])
    a = method.setAccessible(1)
    jar_a = self.jarray.array([u], self.java.lang.Object)
    b = method.invoke(sysloader, [u])
    return u
      
from glob import glob

loader = classPathHacker()
for jar in glob("/var/lib/neo4j/lib/*.jar"):
  loader.addFile(jar)