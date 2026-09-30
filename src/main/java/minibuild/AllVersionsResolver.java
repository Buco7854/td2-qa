package minibuild;
import java.util.HashSet;
import java.util.Set;
public class AllVersionsResolver implements IResolver {
 private final IRegistry registry;
 public AllVersionsResolver(IRegistry registry) { this.registry = registry; }
 public Set<Gav> resolve(Set<Gav> direct) {
  Set<Gav> result = new HashSet<>();
  for (Gav gav : direct) visit(gav, result);
  return result;
 }
 private void visit(Gav gav, Set<Gav> result) {
  Artifact artifact = registry.lookup(gav).orElseThrow();
  result.add(gav);
  for (Gav dep : artifact.dependencies()) visit(dep, result);
 }
}
