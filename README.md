# Spring Dynamic Freemarker

A dynamic query template provider base on [Apache FreeMarker](https://freemarker.apache.org) template engine.

You can refer to [Freemarker Document](https://freemarker.apache.org/docs/index.html) to know more about rules.
Use [Online FreeMarker Template Tester](https://try.freemarker.apache.org) with `tagSyntax = angleBracket` and `interpolationSyntax = dollar` to test your query template.

## Using

You need to configure a FreemarkerQueryTemplateProvider bean to use this template provider.

```java
@Bean
public DynamicQueryTemplateProvider dynamicQueryTemplateProvider() {
    FreemarkerQueryTemplateProvider provider = new FreemarkerQueryTemplateProvider();
    provider.setEncoding("UTF-8");
    provider.setTemplateLocation("classpath:/query");
    provider.setSuffix(".dsql");
    return provider;
}
```

The FreemarkerQueryTemplateProvider has the following parameters:

- `setTemplateLocation`: in case you do not specify the query template on the `@DynamicQuery` annotation, the provider will find it from external template files. The TemplateLocation Is location you put the external template files, default is `classpath:/query`
- `setSuffix`: is the suffix of the external template files, default is `.dsql`. If you don't want the provider to load external templates then set this value to `null`.
- `setEncoding`: is the encoding of templates, default is `UTF-8`
- `setConfiguration`: is a FreemarkerTemplateConfiguration, used to customize Freemarker configuration.

Each template in external template file will start with a template name definition line. The template name definition line must be start with two dash characters (`--`).

## Directives

### Where Directive

`@where` directive knows to only insert `WHERE` if there is any content returned by the containing tags. Furthermore, if that content begins or ends with `AND` or `OR`, it knows to strip it off.

```sql
select t from User t
<@where>
  <#if firstName?has_content>
    and t.firstName = :firstName
  </#if>
  <#if lastName?has_content>
    and t.lastName = :lastName
  </#if>
</@where>
```

### Set Directive

`@set` directive is like the `@where` directive, it removes the commas if it appears at the begins or ends of the content. Also, it will insert `SET` if the content is not empty.

```sql
update User t
<@set>
  <#if firstName?has_content>
    t.firstName = :firstName,
  </#if>
  <#if lastName?has_content>
    t.lastName = :lastName,
  </#if>
</@set>
where t.userId = :userId
```

### Trim Directive

`@trim` directive has four parameters: `prefix`, `prefixOverrides`, `suffix`, `suffixOverrides`.

- `prefix` is the string value that will be inserted at the start of the content if it is not empty, value type is string.
- `prefixOverrides` are values that will be removed if they are at the start of a content, value type is list of strings.
- `suffix` is the string value that will be inserted at the end of the content if it is not empty, value type is string.
- `suffixOverrides` are values that will be removed if they are at the end of a content, value type is list of strings.

 ```sql
<@trim prefix="where (" prefixOverrides=["and ", "or "] suffix=")" suffixOverrides=[" and", " or"]>
  <#if productName?has_content>
    t.productName = :productName or
  </#if>
  <#if category?has_content>
    t.category = :category or
  </#if>
</@trim>
```
