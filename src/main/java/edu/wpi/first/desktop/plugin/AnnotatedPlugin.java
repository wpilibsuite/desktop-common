package edu.wpi.first.desktop.plugin;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A plugin whose descriptor and requirements are specified with {@link Description @Description} and
 * {@link Requires @Requires}, respectively. Annotated plugins are useful because they do not have to be loaded to
 * read their description and requirements, which may cause problems if the plugin has dependencies on another,
 * unloaded, plugin.
 *
 * @param <T> the type of object the plugin applies to
 */
public interface AnnotatedPlugin<T> extends Plugin<T> {

  @Override
  default Descriptor descriptor() {
    Description description = getClass().getAnnotation(Description.class);
    if (description == null) {
      throw new IllegalStateException(
          "Annotated plugin class '" + getClass().getName()
              + "' does not have a @" + Description.class.getName() + " annotation");
    }
    return Descriptor.fromAnnotation(description);
  }

  @Override
  default Collection<Requirement> requirements() {
    Requirements requirements = getClass().getAnnotation(Requirements.class);
    Requires[] requires = getClass().getAnnotationsByType(Requires.class);
    if (requirements == null) {
      return Stream.of(requires)
          .map(Requirement::fromAnnotation)
          .collect(Collectors.toList());
    } else {
      return Stream.concat(Stream.of(requirements.value()), Stream.of(requires))
          .map(Requirement::fromAnnotation)
          .collect(Collectors.toList());
    }
  }
}
