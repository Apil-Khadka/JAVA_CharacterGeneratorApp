GraphQL functionality has been temporarily disabled.

All GraphQL operation files have been moved to the backup folder at:
app/src/main/graphql-backup/

When you're ready to re-enable GraphQL:
1. Restore these files from the backup folder
2. Uncomment Apollo GraphQL plugin and dependencies in build.gradle.kts
3. Make sure your schema.graphqls file has all the required types and operations

Note: This README.txt file ensures the directory structure remains intact while
GraphQL functionality is paused.
