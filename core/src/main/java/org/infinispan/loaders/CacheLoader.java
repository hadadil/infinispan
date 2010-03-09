package org.infinispan.loaders;

import org.infinispan.Cache;
import org.infinispan.container.entries.InternalCacheEntry;
import org.infinispan.marshall.Marshaller;

import java.util.Set;

/**
 * Responsible for loading cache data from an external source
 *
 * @author Manik Surtani
 * @since 4.0
 */
public interface CacheLoader {

   /**
    * Used to initialize a cache loader.  Typically invoked by the {@link org.infinispan.loaders.CacheLoaderManager}
    * when setting up cache loaders.
    *
    * @param config the cache loader configuration bean
    * @param cache  cache associated with this cache loader. Implementations may use this to determine cache name when
    *               selecting where refer to state in storage, for example, a different database table name.
    * @param m      marshaller to use when loading state from a stream, if supported by the implementation.
    */
   void init(CacheLoaderConfig config, Cache<?, ?> cache, Marshaller m) throws CacheLoaderException;

   /**
    * Loads an entry mapped to by a given key.  Should return null if the entry does not exist.  Expired entries are not
    * returned.
    *
    * @param key key
    * @return an entry
    * @throws CacheLoaderException in the event of problems reading from source
    */
   InternalCacheEntry load(Object key) throws CacheLoaderException;

   /**
    * Loads all entries in the loader.  Expired entries are not returned.
    *
    * @return a set of entries, or an empty set if the loader is emptied.
    * @throws CacheLoaderException in the event of problems reading from source
    */
   Set<InternalCacheEntry> loadAll() throws CacheLoaderException;

   /**
    * @param key key to test
    * @return true if the key exists, false otherwise
    * @throws CacheLoaderException in the event of problems reading from source
    */
   boolean containsKey(Object key) throws CacheLoaderException;

   public void start() throws CacheLoaderException;

   public void stop() throws CacheLoaderException;

   /**
    * This method is used by the configuration parser to get a hold of the CacheLoader implementation's corresponding
    * {@link org.infinispan.loaders.CacheLoaderConfig} type. This is usually done by instantiating the CacheLoader
    * and then calling this method.  This may result in 2 instances being created, however, since the instance
    * created to get a hold of the configuration type is then discarded and another instance is created for actual
    * use as a CacheLoader when the cache starts.
    * <p />
    * Since Infinispan 4.1, you can also annotate your CacheLoader implementation with {@link org.infinispan.loaders.CacheLoaderMetadata}
    * and provide this information via the annotation, which will prevent unnecessary instances being created.
    * <p />
    * @return the type of the {@link org.infinispan.loaders.CacheLoaderConfig} bean used to configure this
    *         implementation of {@link org.infinispan.loaders.CacheLoader}.
    */
   Class<? extends CacheLoaderConfig> getConfigurationClass();
}
