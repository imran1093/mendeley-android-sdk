package com.mendeley.api;

import com.mendeley.api.callbacks.RequestHandle;
import com.mendeley.api.callbacks.annotations.AnnotationList;
import com.mendeley.api.callbacks.document.DeleteDocumentCallback;
import com.mendeley.api.callbacks.document.DocumentIdList;
import com.mendeley.api.callbacks.document.DocumentList;
import com.mendeley.api.callbacks.document.GetDocumentsCallback;
import com.mendeley.api.callbacks.document.PatchDocumentCallback;
import com.mendeley.api.callbacks.document.PostDocumentCallback;
import com.mendeley.api.callbacks.document.TrashDocumentCallback;
import com.mendeley.api.callbacks.file.DeleteFileCallback;
import com.mendeley.api.callbacks.file.FileList;
import com.mendeley.api.callbacks.file.GetFileCallback;
import com.mendeley.api.callbacks.file.GetFilesCallback;
import com.mendeley.api.callbacks.file.PostFileCallback;
import com.mendeley.api.callbacks.folder.DeleteFolderCallback;
import com.mendeley.api.callbacks.folder.DeleteFolderDocumentCallback;
import com.mendeley.api.callbacks.folder.FolderList;
import com.mendeley.api.callbacks.folder.GetFolderCallback;
import com.mendeley.api.callbacks.folder.GetFolderDocumentIdsCallback;
import com.mendeley.api.callbacks.folder.GetFoldersCallback;
import com.mendeley.api.callbacks.folder.PatchFolderCallback;
import com.mendeley.api.callbacks.folder.PostDocumentToFolderCallback;
import com.mendeley.api.callbacks.folder.PostFolderCallback;
import com.mendeley.api.callbacks.group.GetGroupCallback;
import com.mendeley.api.callbacks.group.GetGroupMembersCallback;
import com.mendeley.api.callbacks.group.GetGroupsCallback;
import com.mendeley.api.callbacks.group.GroupList;
import com.mendeley.api.callbacks.group.GroupMembersList;
import com.mendeley.api.callbacks.trash.RestoreDocumentCallback;
import com.mendeley.api.exceptions.MendeleyException;
import com.mendeley.api.model.Annotation;
import com.mendeley.api.model.Document;
import com.mendeley.api.model.Folder;
import com.mendeley.api.model.Group;
import com.mendeley.api.model.Profile;
import com.mendeley.api.params.AnnotationRequestParameters;
import com.mendeley.api.params.DocumentRequestParameters;
import com.mendeley.api.params.FileRequestParameters;
import com.mendeley.api.params.FolderRequestParameters;
import com.mendeley.api.params.GroupRequestParameters;
import com.mendeley.api.params.Page;
import com.mendeley.api.params.View;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public interface BlockingSdk {
    /* DOCUMENTS */

    /**
     * Retrieve a list of documents in the user's library.
     */
    DocumentList getDocuments(DocumentRequestParameters parameters) throws MendeleyException;

    /**
     * Retrieve a list of documents in the user's library.
     */
    DocumentList getDocuments() throws MendeleyException;

    /**
     * Retrieve subsequent pages of documents in the user's library.
     *
     * @next reference to next page returned in a previous DocumentList.
     */
    DocumentList getDocuments(Page next) throws MendeleyException;

    /**
     * Retrieve a single document, specified by ID.
     *
     * @param documentId the document id to get.
     * @param view extended document view. If null, only core fields are returned.
     */
    Document getDocument(String documentId, View view) throws MendeleyException;

    /**
     * Retrieve a list of deleted documents in the user's library.
     *
     * @param deletedSince only return documents deleted since this timestamp. Should be supplied in ISO 8601 format.
     * @param parameters holds optional query parameters, will be ignored if null
     */
    DocumentIdList getDeletedDocuments(String deletedSince, DocumentRequestParameters parameters) throws MendeleyException;

    /**
     * Retrieve subsequent pages of deleted documents in the user's library.
     *
     * @next reference to next page returned in a previous DocumentIdList.
     */
    DocumentIdList getDeletedDocuments(Page next) throws MendeleyException;

    /**
     * Add a new document to the user's library.
     *
     * @param document the document object to be added.
     */
    Document postDocument(Document document) throws MendeleyException;

    /**
     * Modify an existing document in the user's library.
     *
     * @param documentId the id of the document to be modified.
     * @param date sets an optional "if unmodified since" condition on the request. Ignored if null.
     * @param document a document object containing the fields to be updated.
     *                 Missing fields are left unchanged (not cleared).
     */
    void patchDocument(String documentId, Date date, Document document) throws MendeleyException;

    /**
     * Move an existing document into the user's trash collection.
     *
     * @param documentId id of the document to be trashed.
     */
    void trashDocument(String documentId) throws MendeleyException;

    /**
     * Delete a document.
     *
     * @param documentId id of the document to be deleted.
     */
    void deleteDocument(String documentId) throws MendeleyException;

    /**
     * Return a list of valid document types.
     */
    Map<String, String> getDocumentTypes() throws MendeleyException;

    /* FILES */

    /**
     * Return metadata for a user's files, subject to specified query parameters.
     */
    FileList getFiles(FileRequestParameters parameters) throws MendeleyException;

    /**
     * Return metadata for all files associated with all of the user's documents.
     */
    FileList getFiles() throws MendeleyException;

    /**
     * Return the next page of file metadata entries.
     *
     * @param next returned from previous getFiles() call.
     */
    FileList getFiles(Page next) throws MendeleyException;

    /* FOLDERS */

    /**
     * Return metadata for all the user's folders.
     */
    FolderList getFolders(FolderRequestParameters parameters) throws MendeleyException;

    /**
     * Return metadata for all the user's folders.
     */
    FolderList getFolders() throws MendeleyException;

    /**
     * Returns the next page of folder metadata entries.
     *
     * @param next returned from a previous getFolders() call.
     */
    FolderList getFolders(Page next) throws MendeleyException;

    /**
     * Returns metadata for a single folder, specified by ID.
     *
     * @param folderId ID of the folder to retrieve metadata for.
     */
    Folder getFolder(String folderId) throws MendeleyException;

    /**
     * Create a new folder.
     *
     * @param folder metadata for the folder to create.
     */
    Folder postFolder(Folder folder) throws MendeleyException;

    /**
     * Update a folder's metadata.
     * <p>
     * This can be used to rename the folder, and/or to move it to a new parent.
     *
     * @param folderId the id of the folder to modify.
     * @param folder metadata object that provides the new name and parentId.
     */
    void patchFolder(String folderId, Folder folder) throws MendeleyException;

    /**
     * Return a list of IDs of the documents stored in a particular folder.
     *
     * @param folderId ID of the folder to inspect.
     */
    DocumentIdList getFolderDocumentIds(FolderRequestParameters parameters, String folderId)
            throws MendeleyException;

    /**
     * Returns the next page of document IDs stored in a particular folder.
     *
     * @param next returned by a previous call to getFolderDocumentIds().
     * @param folderId provides an ID to return in the callback (the value is not actually
     *                 checked by this call).
     */
    DocumentIdList getFolderDocumentIds(Page next, String folderId) throws MendeleyException;

    /**
     * Add a document to a folder.
     *
     * @param folderId the ID the folder.
     * @param documentId the ID of the document to add to the folder.
     */
    void postDocumentToFolder(String folderId, String documentId) throws MendeleyException;

    /**
     * Delete a folder.
     * <p>
     * This does not delete the documents inside the folder.
     *
     * @param folderId the ID of the folder to delete.
     */
    void deleteFolder(String folderId) throws MendeleyException;

    /**
     * Remove a document from a folder.
     * <p>
     * This does not delete the documents itself.
     *
     * @param folderId the ID of the folder.
     * @param documentId the ID of the document to remove.
     */
    void deleteDocumentFromFolder(String folderId, String documentId) throws MendeleyException;

    /* GROUPS */

    /**
     * Return metadata for all the user's groups.
     */
    GroupList getGroups(GroupRequestParameters parameters) throws MendeleyException;

    /**
     * Returns the next page of group metadata entries.
     *
     * @param next returned from a previous getGroups() call.
     */
    GroupList getGroups(Page next) throws MendeleyException;

    /**
     * Returns metadata for a single group, specified by ID.
     *
     * @param groupId ID of the group to retrieve metadata for.
     */
    Group getGroup(String groupId) throws MendeleyException;

    /**
     * Return a list of members user roles of a particular group.
     *
     * @param groupId ID of the group to inspect.
     */
    GroupMembersList getGroupMembers(GroupRequestParameters parameters, String groupId) throws MendeleyException;

    /**
     * Return a list of members user roles of a particular group.
     *
     * @param next returned from a previous getGroupMembers() call.
     */
    GroupMembersList getGroupMembers(Page next) throws MendeleyException;

    /* TRASH */

    /**
     * Retrieve a list of documents in the user's trash.
     */
    DocumentList getTrashedDocuments(DocumentRequestParameters parameters) throws MendeleyException;

    /**
     * Retrieve a list of documents in the user's trash.
     */
    DocumentList getTrashedDocuments() throws MendeleyException;

    /**
     * Retrieve subsequent pages of documents from the user's trash.
     *
     * @next reference to next page returned by a previous DocumentList from getTrashedDocuments().
     */
    DocumentList getTrashedDocuments(Page next) throws MendeleyException;

    /**
     * Move a document from trash into the user's library.
     *
     * @param documentId id of the document to restore.
     */
    void restoreDocument(String documentId) throws MendeleyException;

    /* PROFILES */

    /**
     * Return the user's profile information.
     */
    Profile getMyProfile() throws MendeleyException;

    /**
     * Return profile information for another user.
     *
     * @param  profileId ID of the profile to be fetched.
     */
    Profile getProfile(String profileId) throws MendeleyException;

    /* ANNOTATIONS */

    AnnotationList getAnnotations() throws MendeleyException;

    AnnotationList getAnnotations(AnnotationRequestParameters parameters) throws MendeleyException;

    AnnotationList getAnnotations(Page next) throws MendeleyException;

    Annotation getAnnotation(String annotationId) throws MendeleyException;

    Annotation postAnnotation(Annotation annotation) throws MendeleyException;

    void patchAnnotation(String annotationId, Annotation annotation) throws MendeleyException;

    void deleteAnnotation(String annotationId) throws MendeleyException;
}
